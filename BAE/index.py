import tornado.wsgi
 
class MainHandler(tornado.web.RequestHandler):
    def get(self):
        self.write("Zirconi's TEST SERVER<br /><br />")
        self.write("Usage: /MAGIC?num=[your id]&pwd=[your password]&server=[0 or 1]<br /><br /><br /><br /><br /><br />")
        self.write("Wuhan University of technology Huaxia college<br />SOFTWARE ENGINEERING 1101")
 
app = tornado.wsgi.WSGIApplication([
    (r"/", MainHandler),
])
 
from bae.core.wsgi import WSGIApplication
application = WSGIApplication(app)