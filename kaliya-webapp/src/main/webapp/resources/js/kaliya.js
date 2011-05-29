var host = '127.0.0.1';
var port = 1222;
var socket;

function addtxt(input) {
    var obj=document.getElementById("console");
    var txt=document.createTextNode("> " + input + "\n");
    obj.appendChild(txt);
}

window.onload = function() {
    addtxt("Working...");
    JSocket.init('resources/flash/jSocket.swf', function () {
       socket = new JSocket({
           connectHandler: connectHandler,
           dataHandler:    dataHandler,
           closeHandler:   closeHandler,
           errorHandler:   errorHandler
       });
       socket.connect(host, port);
    });
}

function connectHandler() {
    socket.writeFlush("\n");
}

function dataHandler(data) {
    try {
        var request = JSON.parse(data);
        $.getScript("resources/js/" + request.job + ".js", function() {
            var job = eval(request.job);
            var response = new Object();
            response.id = request.id;
            response.type = request.type;
            response.job = request.job;
            if(request.type == "MAP") {
                response.pairList = job.map(request.key, request.value);
            }
            else {
                response.pair = job.reduce(request.key, request.value);
            }
            socket.writeFlush(JSON.stringify(response)+"\0");
        });
    }
    catch(e) {
        addtxt(data);
    }
}

function closeHandler() {
    addtxt('Lost connection')
}

function errorHandler(errorstr) {
    addtxt("Error: " + errorstr);
}