function initials() {
    formatMargin();
    warnIfButtons();
    addClassToTableHeaderRows();
}

function formatMargin() {
    var body = document.getElementsByTagName("body")[0];
    var mainElements = nodeListToArray(body.childNodes);
    mainElements.splice(2, 1);
    var mainElementsContainer = document.createElement("div");
    mainElementsContainer.id = "mainelementscontainer";
    for (var i = 0; i < mainElements.length; i++) {
        mainElementsContainer.appendChild(mainElements[i]);
    }
    body.appendChild(mainElementsContainer);
}

function nodeListToArray(nodeList) {
    var arr = [];
    for (var i = 0; i < nodeList.length; i++) {
        arr.push(nodeList[i]);
    }
    return arr;
}

function warnIfButtons() {
    if (document.getElementsByTagName("button").length > 0) {
        console.warn("Do not use buttons. Use <a class=\"button\"></a>-structures instead. ");
    }
}

function addClassToTableHeaderRows() {
    var items = document.querySelectorAll("tr th");
    for (var i = 0; i < items.length; i++) {
        var item = items[i];
        var parent = item.parentNode;
        parent.classList.add("has-th");
    }
}