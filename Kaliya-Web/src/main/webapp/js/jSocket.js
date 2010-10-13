// The MIT License

// Copyright (c) 2008 Tjeerd Jan 'Aidamina' van der Molen
// http://jsocket.googlecode.com

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.


// Container that holds the jSocket Objects
var jSocketContainer = [];

// jSocket Constructor
function jSocket(){
    // Random id to identify the socket
    while(true){
        this.id = "jSocket_"+Math.round(Math.random()*10000);
        try{
            // Should throw an exception if it can't find the id in the container
            jSocket_GetSocket(this.id);         
        }
        catch(e){
            // We found an unused id
            break;
        }
    }    
    // Put the jSocket in the container
    jSocketContainer.push({id: this.id, socket:this});     
    
    // Unused variable name used in flash for testing
    // Should use jSocket.variableTest = 'whatever' 
    // If you are using a variable 'xt' in your flashmovie
    this.variableTest ='xt';     
    // Connection state
    this.connected = false;  
}

// Find the Swf object
jSocket.prototype.findSwf = function(){
    if (window.document[this.id]){
        return window.document[this.id];
    }
    if (!$.browser.msi){
        if (document.embeds && document.embeds[this.id])
        return document.embeds[this.id]; 
    }
    else{
        return $("#"+this.id)[0];
    }
}

// Setup the socket
// target: jQuery selector specifying the container that the jSocket will be placed in
jSocket.prototype.setup = function(target)
{
    if(this.target!=undefined) throw 'Can only call setup on a jSocket Object once.';
    this.target = target; 
    
    // Add the object to the dom    
    $(this.target).append("<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\" codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=5,0,0,0\" width=\"1\" height=\"1\" id=\""+this.id+"\"><param name=\"movie\" value=\"flash/jSocket.swf?"+this.id+"\"/><param name=\"allowScriptAccess\" value=\"always\"/><param name=\"quality\" value=\"high\"/><embed src=\"flash/jSocket.swf?"+this.id+"\" quality=\"high\" width=\"1\" height=\"1\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/shockwave/download/index.cgi?P1_Prod_Version=ShockwaveFlash\" name=\""+this.id+"\" swLiveConnect=\"true\"></embed></object>");    
}

// Connect to a listening socket
// host: hostname/ip to connect to
// port: tcp/ip port to connect on
jSocket.prototype.connect = function(host,port){    
    if(!this.movie)
        throw "jSocket isn't ready yet, use the onReady event";
    if(this.connected)
        this.movie.close();
    this.movie.connect(host, port);     
}

//  Close the socket connection
jSocket.prototype.close = function(){
    this.connected = false;
    if(this.movie)
        this.movie.close();    
}

// Find a socket by id in the jSocketContainer
// id: socket id
function jSocket_GetSocket(id){
    var socket = false;
    $.each(jSocketContainer,function()
    {
        if(this.id==id){
            socket = this.socket;
            return false;
        }    
    });
    if(socket)
        return socket;
    // Exception is used in the constructor
    throw "jSocket '"+id+"' not found in jSocketContainer";
}

// Callback for the flash object to signal the flash file is loaded
// triggers jSocket.onReady
function jSocket_onInit(id){
    
    var socket = jSocket_GetSocket(id);
    
    var v = socket.variableTest;
    // Wait until we can actually set Variables in flash
    var f = function(){
        var err = true;
	    try{
	        // Needs to be in the loop, early results might fail, when DOM hasn't updated yet
	        var m = socket.findSwf();
            m.SetVariable(v, 't');
            if('t' != m.GetVariable(v))
                throw null;
            m.SetVariable(v, '');
            // Store the found movie for later use
            socket.movie = m; 
            err=false;
        }
        catch(e){ 
            window.setTimeout(f,0);
        }
        // Fire the event
        if(!err&&socket.onReady)
            socket.onReady();
    }
    window.setTimeout(f,0);
}

// Callback for the flash object to signal data is received
// triggers jSocket.onData
function jSocket_onData(id, size){
    var socket = jSocket_GetSocket(id);
    if(socket.onData)
        socket.onData(size);
}

// Callback for the flash object to signal the connection attempt is finished
// triggers jSocket.onConnect
function jSocket_onConnect(id){
    var socket = jSocket_GetSocket(id);
    socket.connected = true;
    if(socket.onConnect)
        socket.onConnect(true);
}

// Callback for the flash object to signal the connection attempt is finished
// triggers jSocket.onConnect
function jSocket_onError(id, error){
    var socket = jSocket_GetSocket(id);
    if(socket.onConnect)
        socket.onConnect(false,error);
}

// Callback for the flash object to signal the connection was closed from the other end
// triggers jSocket.onClose
function jSocket_onClose(id){
    var socket = jSocket_GetSocket(id);
    socket.connected = false;
    if(socket.onClose)
        socket.onClose();
}


jSocket.prototype.checkConnected = function(){      
    
    if(!this.connected||!this.movie)
        throw "jSocket is not connected, use the onConnect event ";
}

jSocket.prototype.flush = function()
{
    this.checkConnected();
    this.movie.flush();

}
// Generic write
jSocket.prototype.write = function(data)
{
    this.checkConnected();
    this.movie.write(data);

}

// Int
jSocket.prototype.writeInt = function(data){
    if(!this.connected||!this.movie)
        throw "jSocket is not connected, use the onConnect event ";
    return this.movie.writeInt(data);  
}

// Boolean
jSocket.prototype.writeBoolean = function(data){
    this.checkConnected();
	this.movie.writeBoolean(data);			
}

jSocket.prototype.readBoolean = function(){
    this.checkConnected();
	return this.movie.readBoolean();
}

// Byte
jSocket.prototype.writeByte = function(data){
    this.checkConnected();
	this.movie.writeByte(data);
}

jSocket.prototype.readByte = function(){
    this.checkConnected();
	return this.movie.readByte();
}

jSocket.prototype.writeBytes = function(bytes, offset, length){
    this.checkConnected();
	this.movie.writeBytes(bytes, offset, length);	
}

jSocket.prototype.readBytes = function(length){
    this.checkConnected();
	return this.movie.readBytes(length);
}

// Short
jSocket.prototype.writeShort = function(data){			
    this.checkConnected();
	this.movie.writeShort(data);			
}

jSocket.prototype.readShort = function(){
    this.checkConnected();
	return this.movie.readShort();
}

// Int
jSocket.prototype.writeInt = function(data){			
    this.checkConnected();
	this.movie.writeInt(data);			
}

jSocket.prototype.readInt = function(){
    this.checkConnected();
	return this.movie.readInt();
}

// Uint
jSocket.prototype.writeUnsignedInt = function(data){
    this.checkConnected();
	this.movie.writeUnsignedInt(data);
}

jSocket.prototype.readUnsignedInt = function(){
    this.checkConnected();
	return this.movie.readUnsignedInt();
}

// Float
jSocket.prototype.writeFloat = function(data){
    this.checkConnected();
	this.movie.writeFloat(data);
}

jSocket.prototype.readFloat = function(){
    this.checkConnected();
	return this.movie.readFloat();
}

// Double
jSocket.prototype.writeDouble = function(data){
    this.checkConnected();
	this.movie.writeDouble(data);			
}

jSocket.prototype.readDouble = function(){
    this.checkConnected();
	return this.movie.readDouble();
}

// MultiByte		
jSocket.prototype.writeMultiByte = function(value, charSet){
    this.checkConnected();
	this.movie.writeMultiByte(value, charSet);
}

jSocket.prototype.readMultiByte = function(length,charSet){
    this.checkConnected();
	return this.movie.readMultiByte(length, charSet);
}

//UTF		
jSocket.prototype.writeUTFBytes = function(data){
    this.checkConnected();
	this.movie.writeUTFBytes(data);
}		

jSocket.prototype.readUTFBytes = function(length){
    this.checkConnected();
	return this.movie.readUTFBytes(length);	
}

jSocket.prototype.writeUTF = function(data){
    this.checkConnected();
	this.movie.writeUTF(data);
}		

jSocket.prototype.readUTF = function(){
    this.checkConnected();
	return this.movie.readUTF();
}

// Array
jSocket.prototype.writeArray = function(data){
    this.checkConnected();
    this.movie.writeArray(data);
}

// Object
jSocket.prototype.writeObject = function(data){
    this.checkConnected();
	this.movie.writeObject(data);
}

jSocket.prototype.readObject = function(){
	return this.movie.readObject();
}

// Properties
jSocket.prototype.setObjectEncoding = function(value){
	this.movie.setObjectEncoding(value);
}

jSocket.prototype.getObjectEncoding = function(){
	return this.movie.getObjectEncoding();
}

jSocket.prototype.setEndian = function(value){
	this.movie.setEndian(value);
}

jSocket.prototype.getEndian = function(){
	return this.movie.getEndian();
}

jSocket.prototype.getBytesAvailable = function(){
    return this.movie.getBytesAvailable();
}