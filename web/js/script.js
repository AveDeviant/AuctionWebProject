/**
 * Created by Acer on 16.03.2017.
 */
function checkInput() {
    var valid = true;
    var checkedDate = document.addingLot.availableTiming.value;
    var textArea = document.getElementById("description").value;
    var errDate = document.getElementById("errDate");
    var MAX_PRICE = 9999999.99;
    var arr = checkedDate.toString().split("-");
    var year = arr[0];
    var month = arr[1];
    var day = arr[2];
    var date = new Date(year, month, day);
    var currentTime = new Date();
    if (date.getTime() < currentTime) {
        valid = false;
        errDate.innerHTML = '<fmt:message key="admin.lot.timing.err"/> ';
    }
    if (textArea.length > 1000) {
        valid = false;
        var error = document.getElementById("descriptErr");
        error.innerHTML = '<fmt:message key="description.length.error"/>';
    }
    return valid;
}

function checkLength() {
    var textArea = document.getElementById("description").value;
    var count = document.getElementById("symbolCount");
    count.innerHTML = String(1000 - textArea.length);
    if (textArea.length > 1000) {
        count.style.color = "red";
    }
}