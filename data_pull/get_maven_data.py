from bs4 import BeautifulSoup
import urllib.request
from functools import reduce
import re
MAVEN_url="https://mvnrepository.com/artifact"
ARTIFACT_id=""
DEP_lst=[]
ITER=1

def extract_dependency(url,ITER):
    if ITER<=100:
        print(ITER)
        with urllib.request.urlopen(url) as url:
            r = url.read()
        soup = BeautifulSoup(r, 'html.parser')
        title=soup.find("h2", {"class": "im-title"}).findNext("a").get_text().strip(' ')
        compile_dependencies = soup.find("h2", text=re.compile('Compile Dependen.*'))
        table = compile_dependencies.findNext("table")
        rows = table.findAll('tr')
        for tr in rows:
            tmp = ""
            for tag in tr.find_all('a')[-4:]:
                tmp = tmp + tag.getText().strip('\n') + "|"

            print(title+"|"+tmp)
            DEP_lst.append(title+"|"+tmp)
        for i in range(1,50):
            if len(DEP_lst[i].split('|')[1:]) is not None:
                print(DEP_lst[i])
                s_group_id,s_artifact_id,s_version=[str(e) for e in DEP_lst[i].split('|')[1:4]]
                #print (s_group_id+'<->'+s_artifact_id+'<->'+s_version)
                aurl=build_URL(s_group_id,s_artifact_id,s_version)
                extract_dependency(aurl,ITER+1)
        #ITER=ITER+1
        


def build_URL(group_id,artifact_id,version):
    #overwrite artifactid
    #ARTIFACT_id=artifact_id
    urllst=[MAVEN_url,group_id,artifact_id,version]
    return reduce((lambda x,y:x+"/"+y),urllst)

def main():
    group_id="org.apache.hadoop"
    artifact_id="hadoop-core"
    version="1.2.1"
    FINAL_url=build_URL(group_id,artifact_id,version)
    print("Parent URL Parsed : "+FINAL_url)
    extract_dependency(FINAL_url,ITER)
    try:
        if urllib.request.urlopen(FINAL_url):
            print("Scrapping..")
            extract_dependency(FINAL_url,ITER)
            print("complet->>")
        else:
            print("not")
    except:
        print("Not a correct URL")


# Execute main() function
if __name__ == '__main__':
    main()
