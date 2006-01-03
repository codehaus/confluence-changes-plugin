from xmlrpclib import ServerProxy
# simple test program (from the XML-RPC specification)

# server = ServerProxy("http://localhost:8000") # local server
server = ServerProxy("http://localhost:8080/rpc/xmlrpc")

try:
    token = server.confluence1.login("test", "test")
    
    for change in server.changes.getChanges(token, "113620064703"):
      print change
except Exception, v:
    print "ERROR", v