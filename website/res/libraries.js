/* initial function calls */

window.onload = function() {
    this.collapseSyncAll();
}

/* testing */

function helloworld() {
    alert("helloworld");
}


/* document functions */

function element(id) {
    return document.getElementById(id);
}

function value(id) {
    return element(id).value;
}

function deactivateLink(id) {
    var link = element(id);
    link.classList.add("inactive");
    link.setAttribute("onclick", "");
}


/* form functions */

function writeForm(divId, keys, metaPath, buttonLabel, buttonFunction) {
    var div = element(divId);
    var space = document.createElement("br");
    div.appendChild(space);
    var form = createForm(keys, metaPath);
    form.id = "formin:" + divId;
    div.appendChild(form);
    div.appendChild(createSubmitButton(buttonLabel, buttonFunction));
}

function createForm(keys, metaPath) {
    var form = document.createElement("form");
    var meta = getJSONSync("/namesMeta/" + metaPath, null);

    for (var a = 0; a < keys.length; a++) {
        var key = keys[a];
        var label = document.createElement("label");
        label.innerText = getMetalizedName(key, meta);
        form.appendChild(label);
        var space = document.createTextNode(" ");
        form.appendChild(space);
        var input = document.createElement("input");
        input.id = key;
        form.appendChild(input);
        var linebreak = document.createElement("br");
        form.appendChild(linebreak);
    }
    form.remove(form.lastChild);

    return form;
}

function getMetalizedName(givenName, meta) {
    for (var a = 0; a < meta.length; a++) {
        var metaObject = meta[a];
        if (givenName == metaObject.from) return metaObject.to;
    }
    return givenName;
}

function createSubmitButton(buttonLabel, buttonFunction) {
    var button = document.createElement("button");
    button.innerText = buttonLabel;
    button.onclick = buttonFunction;
    return button;
}

function getFormData(formId) {
    var form = element(formId);
    var formData = new FormData();
    var inputFields = form.getElementsByTagName("input");

    for (var a = 0; a < inputFields.length; a++) {
        var inputField = inputFields[a];
        var key = inputField.id;
        var value = inputField.value;
        formData.append(key, value);
    }

    return formData;
}


/* table functions */

function addRows(tableid, rows) {
    var table = element(tableid);
    for (var r = 0; r < rows.length; r++) {
        var row = rows[r];
        var rowObject = document.createElement("tr");
        for (var f = 0; f < rows[r].length; f++) {
            var field = row[f];
            var fieldObject = document.createElement("td");
            fieldObject.innerText = field;
            rowObject.appendChild(fieldObject);
        }
        table.appendChild(rowObject);
    }
}

function addRow(tableid, fields) {
    var table = element(tableid);
    var row = document.createElement("tr");
    for (field in fields) {
        var fieldObject = document.createElement("td");
        fieldObject.innerText = field;
        row.appendChild(fieldObject);
    }
    table.appendChild(row);
}

function addColumn(tableid, headingLabel, fieldLabel) {
    var table = element(tableid);
    var rows = table.childNodes;

    if (headingLabel != "") {
        var headingsRow = rows[0];
        var heading = document.createElement("th");
        heading.innerText = headingLabel;
        headingsRow.appendChild(heading);
    }

    for (var a = 1; a < rows.length; a++) {
        var fieldLabelObject = fieldLabel.cloneNode(true);
        var row = rows[a];
        var field = document.createElement("td");
        if (typeof fieldLabelObject == "string") field.innerHTML = fieldLabelObject;
        else {
            field.appendChild(fieldLabelObject);
        }
        row.appendChild(field);
    }
}

function copyJSON (src) {
    return JSON.parse(JSON.stringify(src));
}

function nodeListToArray(nodeList) {
    var arr = [];
    for (var x = 0; x < nodeList.length; x++) { arr.push(nodeList[x]); }
    return arr;
}

function createTableFromJSON(objs) {
    var table = document.createElement("table");

    if (objs.length > 0) {
        var headingsObject = document.createElement("tr");
        var headings = Object.keys(objs[0]);
        for (heading in headings) {
            var headingObject = document.createElement("th");
            headingObject.innerText = headings[heading];
            headingsObject.appendChild(headingObject);
        }
        table.appendChild(headingsObject);
    }

    for (obj in objs) {
        var row = objs[obj];
        var rowObject = document.createElement("tr");
        for (prop in row) {
            var field = document.createElement("td");
            field.innerText = row[prop];
            rowObject.appendChild(field);
        }
        table.appendChild(rowObject);
    }

    return table;
}

function createArrayFromJSON(json) {
    var array = [];
    for (field in json) {
        array.push(field);
    }
    return array;
}

function writeTable(divid, json) {
    var div = element(divid);
    var table = createTableFromJSON(json);
    table.setAttribute("id", "tablein:" + divid);
    div.appendChild(table);
}


/* tables meta data */

function metalizeTable(tableId, metaPath) {
    var table = element(tableId);
    var removementsMeta = getJSONSync("/removementsMeta/" + metaPath, null);
    removeHeadings(table, removementsMeta);
    var namesMeta = getJSONSync("/namesMeta/" + metaPath, null);
    formatHeadings(table, namesMeta);
}

function removeHeadings(table, removesMeta) {
    var rows = table.childNodes;
    var headingObjects = rows[0].childNodes;
    var removeColumns = [];
    for (a in removesMeta) {
        var removeMeta = removesMeta[a];
        for (b in headingObjects) {
            var headingObject = headingObjects[b];
            if (headingObject.innerText == removeMeta) removeColumns.push(b);
        }
    }
    removeColumns.reverse();
    for (a in removeColumns) {
        var removeColumn = removeColumns[a];
        for (var b = 0; b < rows.length; b++) {
            var row = rows[b];
            var field = row.childNodes[removeColumn];
            row.removeChild(field);
        }
    }
}

function formatHeadings(table, namesMeta) {
    var headingObjects = table.childNodes[0].childNodes;
    for (a in namesMeta) {
        var nameMeta = namesMeta[a];
        for (b in headingObjects) {
            var headingObject = headingObjects[b];
            if (headingObject.innerText == nameMeta.from) headingObject.innerText = nameMeta.to;
        }
    }
}


/* collapsing areas */

function createCollapseData(collId, shown, hidden) {
    var collapseDataEntity = {};

    var shownId = Math.random().toString();
    collapseDataEntity.shown = shownId;
    collapseDivs.set(shownId, shown);

    var hiddenId = Math.random().toString();
    collapseDataEntity.hidden = hiddenId;
    collapseDivs.set(hiddenId, hidden);

    collapseData.set(collId, collapseDataEntity);
}

function getCollapseDiv(collapseDataEntity, attr) {
    if (attr == "shown") var divId = collapseDataEntity.shown;
    else var divId = collapseDataEntity.hidden;
    return collapseDivs.get(divId);
}

function findCollapseDiv(collId) {
    var divs = document.getElementsByTagName("div");
    for (var i = 0; i < divs.length; i++) {
        var div = divs[i];
        if (div.hasAttribute("coll-id")) {
            if (div.getAttribute("coll-id") == collId) return div;
        }
    }
}

var collapseData = new Map();
var collapseDivs = new Map();

function collapseSyncAll() {
    var divs = document.getElementsByTagName("div");
    for (var i = 0; i < divs.length; i++) {
        var div = divs[i];
        if (div.hasAttribute("coll-id")) {
            console.log(div);
            var collId = div.getAttribute("coll-id");
            if (div.hasAttribute("coll-distributor")) {
                if (!div.hasAttribute("coll-state")) div.setAttribute("coll-state", "hidden");

                var shownId = div.getAttribute("coll-shown");
                var shownDiv = findCollapseDiv(shownId);
                console.log(shownDiv);
                var hiddenId = div.getAttribute("coll-hidden");
                var hiddenDiv = findCollapseDiv(hiddenId);
                console.log(hiddenDiv);
                createCollapseData(collId, shownDiv, hiddenDiv);
                console.log(collapseData.get(collId));
                console.log(collapseData);

                collapseSync(collId);
            } else if (!div.hasAttribute("coll-div")) {
                if (!div.hasAttribute("coll-state")) div.setAttribute("coll-state", "hidden");
                collapseSync(collId);
            }
        }
    }
}

function collapseSync(collId) {
    var div = findCollapseDiv(collId);
    var shown = div.getAttribute("coll-state") == "shown";
    if (div.hasAttribute("coll-distributor")) {
        console.log("8");
        var collapseDataEntity = collapseData.get(collId);
        var shownDiv = getCollapseDiv(collapseDataEntity, "shown");
        console.log(shownDiv);
        var hiddenDiv = getCollapseDiv(collapseDataEntity, "hidden");
        if (shown) {
            hiddenDiv.remove();
        } else {
            shownDiv.remove();
        }
    } else if (!div.hasAttribute("coll-div")) {
        console.log("9");
        collapseDivs.set(collId, div);
        if (!shown) {
            console.log("7");
            div.setAttribute("coll-state", "shown");
            collapseToggle(collId);
        }
    }
}

/*
function collapseSync(collId) {
    var collapseDiv;
    var elements = document.getElementsByTagName("div");
    for (var i = 0; i < elements.length; i++) {
        var element = elements[i];
        if (element.getAttribute("coll-id") == collId) collapseDiv = element;
    }

    var shown = collapseDiv.getAttribute("coll-state") == "shown";
    if (!shown) {
        collapseDiv.setAttribute("coll-state", "shown");
        collapseToggle(collId);
    }
}
*/

function collapseToggle(collId) {
    console.log("coll-id: " + collId);
    var div = findCollapseDiv(collId);
    var shown = div.getAttribute("coll-state") == "shown";
    if (div.hasAttribute("coll-distributor")) {
        console.log("1");
        var collapseDataEntity = collapseData.get(collId);
        var shownDiv = getCollapseDiv(collapseDataEntity, "shown");
        var hiddenDiv = getCollapseDiv(collapseDataEntity, "hidden");
        if (shown) {
            console.log("2");
            shownDiv.parentNode.insertBefore(hiddenDiv, shownDiv);
            shownDiv.remove();
            div.setAttribute("coll-state", "hidden");
        } else {
            console.log("3");
            hiddenDiv.parentNode.insertBefore(shownDiv, hiddenDiv);
            hiddenDiv.remove();
            div.setAttribute("coll-state", "shown");
        }
    } else {
        console.log("4");
        if (shown) {
            console.log("5");
            collapseDivs.set(collId, div);
            var marker = document.createElement("div");
            marker.setAttribute("coll-id", collId);
            marker.setAttribute("coll-state", "hidden");
            var parent = div.parentNode;
            parent.insertBefore(marker, div);
            console.log("should work");
            div.remove();
        } else {
            console.log("6");
            var collapseContent = collapseDivs.get(collId);
            var parent = div.parentNode;
            parent.insertBefore(collapseContent, div);
            div.remove();
        }
    }
}


/* http.js */

function get(path, handling) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            handling(this.responseText);
        }
    }
    xhttp.open("GET", path);
    xhttp.send();
}

function getJSON(path, handling) {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            handling(JSON.parse(this.responseText));
        }
    }
    xhttp.open("GET", path);
    xhttp.send();
}

function getSync(path, body) {
    var xhttp = new XMLHttpRequest();
    xhttp.open("GET", path, false);
    xhttp.send(body);
    if (xhttp.status = 200) {
        return xhttp.responseText;
    }
}

function getJSONSync(path, body) {
    var responseText = getSync(path, body);
    return JSON.parse(responseText);
}

function post(path, body) {
    console.log("post path:" + path);
    console.log(body);
    var xhttp = new XMLHttpRequest();
    xhttp.open("POST", path);
    xhttp.send(body);
}

function hyperlink(url) {
    window.location.href = url;
}