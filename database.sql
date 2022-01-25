drop schema if exists itech_library;

create database itech_library;
use itech_library;

-- -------------------books-------------------------

create table books(
id bigint auto_increment primary key,
title_ru nvarchar(100) not null,
title_origin nvarchar(100) null,
description nvarchar(150) null,
book_cost decimal(10, 2) not null,
day_cost decimal(10, 2) not null,
edition_year int null,
page_count int null,
-- total_quantity int default 0, -- ?
-- available_quantity int default 0, -- ?
registration_date date null
);

create table book_cover_photos(
id bigint auto_increment primary key,
book_id bigint not null,
cover_photo_path nvarchar(200) not null,
constraint book_cover_photos_book_id_FK foreign key (book_id) references books(id) on delete cascade
);

-- -----------------------genres----------------------------

create table genres(
id bigint auto_increment primary key,
genre_name nvarchar(50) unique not null
);
insert into genres (genre_name) values
('классика'),
('фантастика'),
('научно-популярный'),
('историко-документальный'),
('детектив'),
('приключения'),
('роман'),
('повесть'),
('поэзия'),
('профессиональная литература'),
('религиозный'),
('учебная литература'),
('техника');

create table book_genres(
id bigint auto_increment primary key,
book_id bigint not null,
genre_id bigint not null,
constraint book_genres_book_id_FK foreign key (book_id) references books(id) on delete cascade,
constraint book_genres_genre_id_FK foreign key (genre_id) references genres(id)
);

-- ---------------------authors-------------------------
create table authors
(
    id bigint auto_increment primary key,
    name nvarchar(50) not null,
    surname nvarchar(50) not null,
    photo_path nvarchar(200) null 
);

create table book_authors
(
    id bigint auto_increment primary key,
    book_id bigint not null,
    author_id bigint not null,
    constraint book_authors_book_id_FK foreign key (book_id) references books(id) on delete cascade,
    constraint book_authors_author_id_FK foreign key (author_id) references authors(id)
);

-- ------------------------book_copies---------------------------
create table book_copies
(
id bigint auto_increment primary key,
book_id bigint not null,
copy_status nvarchar(20) not null default 'available', -- 'available', 'borrowed', 'deleted'
copy_state nvarchar(20) not null default 'normal', -- 'normal', 'damaged'
constraint book_copies_book_id_FK foreign key (book_id) references books(id) on delete cascade
);

-- ------------------------readers-------------------------------------
create table readers
(
    id bigint auto_increment primary key,
    first_name nvarchar(50) not null,
    last_name nvarchar(50) not null,
    middle_name nvarchar(50) null,
    passport_number nvarchar(30) unique null,    
    birth_date date not null,
    email nvarchar(50) unique not null,
    address nvarchar(200) null   
);

-- -------------------------borrows------------------------------------
create table borrows
(
    id bigint auto_increment primary key,
    reader_id bigint not null,
    borrow_date date not null,
    borrow_time_period_days int not null,
    return_date date null,
    borrow_status nvarchar(20) not null default 'active', -- 'active', 'returned', 'expired'
    discount_percent int null,                                      -- init outside
    cost decimal(5,2) null,                                 -- init outside
    constraint borrows_reader_id_FK foreign key(reader_id) references readers(id)    
);

create table borrow_books_copy
(
    id bigint auto_increment primary key,
    borrow_id bigint not null,
    book_copy_id bigint not null,
    book_copy_rating decimal(4,1) null,    
    constraint borrow_books_copy_borrow_id_FK foreign key(borrow_id) references borrows(id),
    constraint borrow_books_copy_book_copy_id_FK foreign key(book_copy_id) references book_copies(id) on delete cascade    
);

create table borrow_books_copy_damage_photos
(
    id bigint auto_increment primary key,
    borrow_books_copy_id bigint not null,
    damage_photo nvarchar(200) not null,
    constraint borrow_books_copy_damage_photos_borrow_books_copy_id_FK foreign key (borrow_books_copy_id) references borrow_books_copy(id) on delete cascade   
);







