function setup()
{
    let dateInput = document.getElementById("registrationDate");
    dateInput.value = new Date().toISOString().slice(0,10);
    dateInput.readOnly = true;
}

setup();