// Host we are connecting to
var host = '127.0.0.1';
// Port we are connecting on
var port = 1222;

var encodeString = "";

var socket = new jSocket();

function addtxt(input) {
    var obj=document.getElementById("console");
    var txt=document.createTextNode(input+"\n");
    obj.appendChild(txt);
}

// When the socket is added the to document
socket.onReady = function(){
    socket.connect(host,port);
}

// Connection attempt finished
socket.onConnect = function(success){
    if(success) {
        addtxt("Connected to server");
        write('\n');
    } else {
        addtxt('Connection to the server could not be estabilished');
    }
}
socket.onData = function(data){
    var t = socket.readUTFBytes(socket.getBytesAvailable());
    /*if(t.length < 100) {
     addtxt("Raw: " + t);
     }*/
    if(t.substring(0,3) == "OK!"){
        write('GETENCODESTRING');
        addtxt("Cracking something please wait...");
    } else if(t.substring(0,4) == "STOP"){
        addtxt("Connection closed");
        socket.close();
    } else if(t.substring(0,12) == "ENCODESTRING") {
        encodeString = t.substring(13);
        encodeString = encodeString.replace(/^\s+|\s+$/, '');
        addtxt("String to crack: " + encodeString);
        write('GETBLOCK');
    } else if(t.substring(0,6) == "ANSWER"){
        addtxt("md5(\"" + t.substring(7).replace(/^\s+|\s+$/, '') + "\") = \"" + encodeString + "\"");
        write('GETENCODESTRING');
    } else {
        crackit(eval(t));
    }
}

function write(str){
    socket.writeUTF(str);
    socket.writeUTF('');
}

function crackit(list){
    for(i = 0;i<1000;++i){
        if(md5(list[i]) == encodeString){
            var r = "FOUND "+list[i];
            write(r);
            return;
        }
    }
    write('GETBLOCK');
}
// Setup our socket in the div with the id="socket"
socket.setup('#socket');