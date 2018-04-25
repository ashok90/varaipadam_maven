from bs4 import BeautifulSoup
import urllib.request
from functools import reduce
import re
import time

EXPORT_path="/Users/ashokkuppuraj/Documents/IndianaUniv/Spring2018/SQL-no-SQL/Final_project/Project/srcdata/"
MAVEN_url="https://mvnrepository.com/artifact"
ARTIFACT_id=""
DEP_lst=[]
ITER=0
START_url=""
file = open(EXPORT_path+"link","w")

def extract_dependency(url):
    ##function to load the dependencies in the GLOBAL list DEP_lst
    global ITER
    if ITER<=10:
        print(url)
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
                DEP_lst.append(title+"|"+tmp)
        ITER+=1
        flow_cntrl()
            
def flow_cntrl():
    ##function to start the extract_dependency function and write the GLOBAL list DEP_lst to a .csv FILE
    global DEP_lst
    global START_url
    tlst=[]
   
    if ITER==0:
        #First time run the extract dependency function
        extract_dependency(START_url)
    else:
        
        print("inside"+str(ITER))
        #write the file
        for i in DEP_lst:
            if len(i.split('|'))==5:
                print(i)
                #print(i.split('|')[1:4])
                file.write(i+'\n')
                tlst.append(i)
        
        #print(DEP_lst)
        DEP_lst=[]
        #Iterate for the dependent URLs
        for j in tlst:
            s_group_id,s_artifact_id,s_version=j.split('|')[1:4]
            url=build_URL(s_group_id,s_artifact_id,s_version)
            if urllib.request.urlopen(url):
                time.sleep(5)
                extract_dependency(url)

def build_URL(group_id,artifact_id,version):
    urllst=[MAVEN_url,group_id,artifact_id,version]
    return reduce((lambda x,y:x+"/"+y),urllst)

def main():
    group_id="org.apache.hadoop"
    artifact_id="hadoop-core"
    version="1.2.1"
    global START_url
    global file
    START_url=build_URL(group_id,artifact_id,version)
    print("Parent URL Parsed : "+START_url)
    #extract_dependency(FINAL_url,ITER)

    #extract_dependency(FINAL_url)
    flow_cntrl()
    file.close()


# Execute main() function
if __name__ == '__main__':
    main()
