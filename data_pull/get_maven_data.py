from bs4 import BeautifulSoup
import urllib.request
from functools import reduce
import re

def extract_dependency(url)
    with urllib.request.urlopen('https://mvnrepository.com/artifact/org.apache.hadoop/hadoop-core/1.2.1') as url:
        r = url.read()
soup = BeautifulSoup(r, 'html.parser')

title=soup.find('title').getText()

compile_dependencies = soup.find("h2", text=re.compile('Compile Dependen.*'))

table = compile_dependencies.findNext("table")

rows = table.findAll('tr')
print(title)
for tr in rows:
    #print(tr)
    tmp = ""
    for tag in tr.find_all('a')[-3:]:
        tmp = tmp + tag.getText().strip('\n') + " "
    print(tmp)



def main():


it=[MAVEN_url,group_id,artifact_id,version]
MAVEN_url="https://mvnrepository.com/artifact"
group_id="org.apache.hadoop"
artifact_id="hadoop-core"
version="1.2.1"
FINAL_url=reduce((lambda x,y:x+"/"+y),it)
print(FINA_url)
try:
    if urllib.request.urlopen(FINAL_url):
        print("Scrapping..")
        extract_dependency(FINAL_url)
    else:
        print("not")
except:
    print("Not a correct URL")


# Execute main() function
if __name__ == '__main__':
    main()
