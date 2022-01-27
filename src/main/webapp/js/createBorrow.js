function setup()
{
    let returnDateInput = document.getElementById("returnDate");

    let date = new Date();
    date.setMonth(date.getMonth() + 1);
    returnDateInput.value = date.toISOString().slice(0, 10);
    returnDateInput.readOnly = true;
}

setup();