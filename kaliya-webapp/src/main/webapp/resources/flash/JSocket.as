/* NAME
 *    JSocket - Socket on Javascript
 * AUTHOR
 *    Masahiro Chiba<nihen@megabbs.com>
 * MODIFIED BY
 *    Philippe L'Heureux<philippe.lheureux@umontreal.ca>
 *
 */

package {
    import flash.display.Sprite;
    import flash.events.*;
    import flash.net.Socket;
    import flash.system.Security;
    import flash.utils.*;

    import flash.external.*;

    public class JSocket extends Sprite
    {
        private static const ESCAPER_REG:RegExp = /\\/g;
        private static var sockets:Array = new Array();
        private static var handlers:Object;

        public function JSocket():void {
            ExternalInterface.addCallback('newsocket', newsocket);
            ExternalInterface.addCallback('connect', connect);
            ExternalInterface.addCallback('write', write);
            ExternalInterface.addCallback('flush', flush);
            ExternalInterface.addCallback('writeFlush', writeFlush);
            ExternalInterface.addCallback('close', close);

            ExternalInterface.call('JSocket.swfloaded');
        }

        public static function newsocket(dummy:Boolean = false):int {
            var soc:Socket = new Socket();

            sockets.push(soc);
            var socid:int = sockets.length - 1;

            soc.addEventListener(Event.CONNECT,                     function (e:Event):void { connectHandler(e, socid) });
            soc.addEventListener(ProgressEvent.SOCKET_DATA,         function (e:Event):void { dataHandler(e, socid) } );
            soc.addEventListener(Event.CLOSE,                       function (e:Event):void { closeHandler(e, socid); } );
            soc.addEventListener(IOErrorEvent.IO_ERROR,             function (e:Event):void { errorHandler(e, socid); } );
            soc.addEventListener(SecurityErrorEvent.SECURITY_ERROR, function (e:Event):void { errorHandler(e, socid); } );

            return socid;
        }

        public static function connect(socid:int, host:String, port:int):void {
            setTimeout(doConnect, 1, socid, host, port);
        }
        private static function doConnect(socid:int, host:String, port:int):void {
            sockets[socid].connect(host, port);
        }

        public static function write(socid:int, data:String):void {
            setTimeout(doWrite, 1, socid, data);
        }
        private static function doWrite(socid:int, data:String):void {
            sockets[socid].writeUTF(data);
        }

        public static function writeFlush(socid:int, data:String):void {
            setTimeout(doWriteFlush, 1, socid, data);
        }
        private static function doWriteFlush(socid:int, data:String):void {
            sockets[socid].writeUTF(data);
            sockets[socid].flush();
        }

        public static function flush(socid:int):void {
            setTimeout(doFlush, 1, socid);
        }
        public static function doFlush(socid:int):void {
            sockets[socid].flush();
        }

        public static function close(socid:int):void {
            setTimeout(doClose, 1, socid);
        }
        private static function doClose(socid:int):void {
            sockets[socid].close();
        }

        private static function connectHandler(event:Event, socid:int):void {
            ExternalInterface.call('JSocket.connectHandler', socid);
        }
        private static function closeHandler(event:Event, socid:int):void {
            ExternalInterface.call('JSocket.closeHandler', socid);
        }

        private static function dataHandler(event:Event, socid:int):void {
            var soc:Socket = sockets[socid];
            var string:String = "";
            try {
	            while(soc.bytesAvailable) {
	            	string = soc.readUTF();
	            	ExternalInterface.call('JSocket.dataHandler', socid, string.replace(ESCAPER_REG, '\\\\'));
	            }
            } 
            catch(e:Error) {
            	ExternalInterface.call('JSocket.errorHandler', socid, e.toString() + " string: " + string.replace(ESCAPER_REG, '\\\\'));
            	return;
            }	
        }

        private static function errorHandler(event:Event, socid:int):void {
            ExternalInterface.call('JSocket.errorHandler', socid, event.toString().replace(ESCAPER_REG, '\\\\'));
        }
    }
}
