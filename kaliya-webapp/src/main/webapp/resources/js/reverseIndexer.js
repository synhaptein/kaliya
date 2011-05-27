function reverseIndexer() {}

reverseIndexer.map = function(key, value) {
    var ret = [];
    var words = normalizeText(value).split(' ');
    for (var i=0; i<words.length; i++) {
        ret.push({key:words[i], value:key});
    }
    return ret;
};

reverseIndexer.reduce = function(intermediateKey, values) {
    var files = new Array();
    var filesSet = new Object();
    for (var i=0; i<values.length; i++) {
        filesSet[values[i]] = true;
    }
    for (var file in filesSet) {
        files.push(file);
    }
    return {key:intermediateKey, value:files};
};

function normalizeText(s) {
    s = s.toLowerCase();
    s = s.replace(/[^a-z]+/g, ' ');
    return s;
}