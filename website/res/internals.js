var pathVariables = [];

function setMeta(key, value) {
    switch (key) {
        case "title": document.title = value; break;
    }
}

function putPathVariable(pathVariable) {
    pathVariables.push(pathVariable);
}

function getPathVariables() {
    return pathVariables;
}