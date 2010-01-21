from xmlrpclib import ServerProxy
# simple test program (from the XML-RPC specification)

# server = ServerProxy("http://localhost:8080/rpc/xmlrpc")
server = ServerProxy("http://docs.codehaus.org/rpc/xmlrpc")

try:
    token = server.confluence1.login("confluenza", "confluenza42")
    lastChange = long(server.changes.getSystemTime(token)) - 24 * 3600 * 1000
    
    for change in server.changes.getChanges(token, str(lastChange)):
      print change
except Exception, v:
    print "ERROR", v
