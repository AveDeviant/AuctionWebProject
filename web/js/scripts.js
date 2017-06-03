function showBets() {
    var bets = document.getElementById("bets");
    if (bets.style.display === "none") {
        bets.style.display = "inline";
    }
    else {
        bets.style.display = "none";
    }
}
function showComments() {
    var comments = document.getElementById("comments");
    if (comments.style.display === "none") {
        comments.style.display = "inline";
    }
    else {
        comments.style.display = "none";
    }
}

/**
 *
 * @returns {boolean} <code>true</code> if entered data is valid.
 * <code>false</code> in other case.
 */
function checkInput() {
    var valid = true;
    var checkedDate = document.addingLot.availableTiming.value;
    var textArea = document.getElementById("description").value;
    var errDate = document.getElementById("errDate");
    var MAX_BIDDING_DATE = 30;
    var arr = checkedDate.toString().split("-");
    var year = arr[0];
    var month = arr[1] - 1;
    var day = arr[2];
    var date = new Date(year, month, day);
    var currentTime = new Date();
    if ((date.getTime() < currentTime) || ((date.getTime() - currentTime.getTime()) / 86400000) > MAX_BIDDING_DATE) {
        valid = false;
        errDate.style.display = "block";
    }
    if (textArea.length > 1000) {
        valid = false;
        var error = document.getElementById("descriptErr");
        error.style.display = "block";
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