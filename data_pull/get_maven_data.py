from bs4 import BeautifulSoup
import urllib.request
from functools import reduce
import re
import time
import sys



def extract_dependency(url):
    ##function to load the dependencies in the GLOBAL list DEP_lst
    l = []
    URL=str(url)
    file1 = open("/Users/ashokkuppuraj/Documents/IndianaUniv/Spring2018/SQL-no-SQL/Final_project/Project/srcdata/node.txt","w") 
    file2 = open("/Users/ashokkuppuraj/Documents/IndianaUniv/Spring2018/SQL-no-SQL/Final_project/Project/srcdata/link.txt","w")
    try:
        if urllib.request.urlopen(url):
            with urllib.request.urlopen(url) as url:
                r = url.read()
                soup = BeautifulSoup(r, 'html.parser')
                title=soup.find("h2", {"class": "im-title"}).findNext("a").get_text().strip(' ')
                tmp1='|'.join(URL.split('/')[-3:])
                compile_dependencies = soup.find("h2", text=re.compile('Compile Dependen.*'))
                ##-----write the file 1 
                file1.write(title+'|'+tmp1+'|'+re.findall(r'\b\d+\b',compile_dependencies.get_text())[0])
                file1.close()
                table = compile_dependencies.findNext("table")
                rows = table.findAll('tr')
                for tr in rows:
                    tmp = ""
                    for tag in tr.find_all('a')[-4:]:
                        tmp = tmp + tag.getText().strip('\n') + "|"
                    tmp = title + '|' + tmp
                    print(tmp)
                    ##-----write the file 2
                    file2.write(tmp+'\n')
                    l.append(tmp)
        file2.close()
        return l
    except:
        print("Unexpected error:", sys.exc_info()[0])
                

def build_url(group_id,artifact_id,version):
    maven_url="https://mvnrepository.com/artifact"
    urllst=[maven_url,group_id,artifact_id,version]
    return reduce((lambda x,y:x+"/"+y),urllst)

def get_parser():
    from argparse import ArgumentParser, ArgumentDefaultsHelpFormatter
    parser = ArgumentParser(description=__doc__,
                            formatter_class=ArgumentDefaultsHelpFormatter)
    parser.add_argument('-g', '--group', dest='group_id')
    parser.add_argument('-a', '--artifact', dest='artifact_id')
    parser.add_argument('-v', '--version', dest='version')
    return parser

def extract_dependency_master(group_id, artifact_id, version):
    l = []
    tmp = "|"  + group_id + "|" + artifact_id + "|" + version
    l.append(tmp)
    while l:
        popped = l.pop()
        print(popped)
        try:
            s_group_id,s_artifact_id,s_version=popped.split('|')[1:4]
            url=build_url(s_group_id,s_artifact_id,s_version)
            l.extend(extract_dependency(url))
            #extract_dependency(url)
            #print(l)
        except:
            pass

def main():
    #org.eclipse.jdt|core|3.1.1
    parser = get_parser()
    if (len(sys.argv) < 2):
        parser.print_help()
    else:
        #args = parser.parse_args()
        group_id="org.apache.hadoop"
        #group_id = args.group_id
        artifact_id="hadoop-core"
        #artifact_id = args.artifact_id
        version="1.2.1"
        #version = args.version
        extract_dependency_master(group_id,artifact_id,version)
        print("Completed")

# Execute main() function
if __name__ == '__main__':
    main()
