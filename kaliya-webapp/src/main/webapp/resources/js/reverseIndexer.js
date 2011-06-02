/**
 * This is only for the md5cracker part that implement the mapreduce algorithm to bruteforce a md5 hash.
 *
 * Inspired by
 * Sean, MapReduce with JavaScript
 * http://www.dashdashverbose.com/2009/01/mapreduce-with-javascript.html
 *
 * Inspired by
 * Michael Nielsen, Write your first MapReduce program in 20 minutes
 * http://michaelnielsen.org/blog/write-your-first-mapreduce-program-in-20-minutes/
 *
 * Redistributions of files must retain the above copyright notice.
 *
 * @copyright     Copyright 2010-2011, SynHaptein (http://www.synhaptein.com)
 * @link          http://www.synhaptein.com/kaliya kaliya project
 * @since         kaliya 0.1
 * @license       http://www.synhaptein.com/kaliya/license.html
 */

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