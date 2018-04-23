from bs4 import BeautifulSoup
import urllib.request
import re

with urllib.request.urlopen('https://mvnrepository.com/artifact/org.apache.hadoop/hadoop-core/1.2.1') as url:
    r = url.read()
soup = BeautifulSoup(r, 'html.parser')

soup.find('title').getText()

compile_dependencies = soup.find("h2", text=re.compile('Compile Dependen.*'))

table = compile_dependencies.findNext("table")

rows = table.findAll('tr')

for tr in rows:
    #print(tr)
    tmp = ""
    for tag in tr.find_all('a')[-3:]:
        tmp = tmp + tag.getText().strip('\n') + " "
    print(tmp)

