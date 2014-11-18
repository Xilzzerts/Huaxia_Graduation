__author__ = 'Zirx'

import xml.dom.minidom

doc = xml.dom.minidom.Document()

resource = doc.createElement('resources')
doc.appendChild(resource)
string = doc.createElement('string')
string.setAttribute('name', 'hello_world')
value = doc.createTextNode('HelloWorld')
string.appendChild(value)
resource.appendChild(string)
print(doc.toprettyxml(encoding='utf-8'))


"""
OUTPUT:
b'<?xml version="1.0" encoding="utf-8"?>\n<string name="hello">HelloWorld</string>\n'

xml.dom.minidom is a minimal implementation of the Document Object Model interface,
with an API similar to that in other languages.
It is intended to be simpler than the full DOM and also significantly smaller.
Users who are not already proficient with the DOM should consider using the
xml.etree.ElementTree module for their XML processing instead
"""