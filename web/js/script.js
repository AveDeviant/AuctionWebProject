/**
 * Created by Acer on 16.03.2017.
 */
function checkDate() {
    var valid =true;
    var checkedDate = document.addingLot.availableTiming.value;
    var errDate = document.getElementById("errDate");
    var arr = checkedDate.toString().split("-");
    var year=arr[0];
    var month=arr[1];
    var day=arr[2];
    var date = new Date(year,month,day);
    var currentTime = new Date();
    if (date.getTime()< currentTime) {
        valid =false;
        errDate.style.display="inline";
    }
    return valid;
}
function showCardForm() {
    var form = document.getElementById("cardForm");
    if (form.style.display === "none") {
        form.style.display = "inline";
    } else {
        form.style.display = "none";
    }
}

function showBets() {
    var bets = document.getElementById("bets");
    if (bets.style.display === "none") {
        bets.style.display = "inline";
    }
    else {
        bets.style.display = "none";
    }
}
