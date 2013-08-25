package webServer;


import Log.WriteLog;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import fileInfo.FileInfo;
import fileInfo.FileInfoDatabase;
import search.FindLANShared;
import search.NetFiles;
import search.Recommendation;
import search.SearchKeyword;
/**
 * GET and Post request will be redirected hare.
 * GET and Post are not synchronized.
 * @author kamal
 *
 */
public class ProcessHttp implements ProcessHttpReq {
		private SearchKeyword sk;
		private FileInfoDatabase database;
		private Recommendation rec;
		
	/**
	 * Searching Interface and Database of Files is needed.
	 * @param sk To Search
	 * @param database To get Files Information
	 * @param rec Interface Recommendation to get Recommendation
	 */
		public ProcessHttp(SearchKeyword sk,FileInfoDatabase database,Recommendation rec){
			this.sk=sk;
			this.database=database;
			this.rec=rec;
		}
	
		
        /**
         * check validity of URL 
         * check if anyone accessing restricted folders.
         * @param s URL
         * @return true if valid.
         */
        protected boolean isValidURL(String s){
           //TO DO http://localhost/midframe.html?//192.168.1.164/C$ write a program to restrict this 
        	//To Do write a program to restrict users
        	
        	//Check search query
        	s=s.toLowerCase();
            return (!(s.contains("$")||s.contains("users")));//can not access address which contain "$"
        }
    
        /**
         * Show files within List in midframe.html. 
         * @param list
         * @param out
         * @throws Exception 
         */
        protected void showSearchResultList(List<File> list,PrintStream out) throws Exception{
            
            out.println("HTTP/1.1 200 OK");
            out.println("content-type: text/html");
            out.println();
            list=CompareFiles.sort(list,database);
            BufferedReader br=new BufferedReader(new InputStreamReader(new ReadFile("/midframe.html/").getInputStream()));
            
            while(true){
                String l=br.readLine();
                if(l.indexOf("<%%>")!=-1){
                    break;
                }
                out.println(l);
            }
            
            out.println("<h2>"+"Result/Recommendations"+"</h2>");
            Iterator<File> i=list.iterator();
            while(i.hasNext()){
                File ff=i.next();
                long ct=0;
                double rating=0;
                double avRating=0;
                long noOfUsers=0;
                try{
                	FileInfo vv=database.getFile(ff);
                	ct=vv.getDownloads();
                	rating=vv.getRating();
                	avRating=vv.getAvRating();
                	noOfUsers=vv.getNoOfUser();
                }
                catch(FileNotFoundException e){
                	
                }
                String link=ff.isDirectory()?"\"midframe.html?//"+ff.getAbsolutePath().substring(2) +"/\"":"\"midframe.html?file:://"+ff.getAbsolutePath().substring(2)+"/\"";
                out.print("<li><a href="+link+">"+ff.getName()+"</a> <span class='info'>("+(ff.isDirectory()?"Folder ) Opened="+ct:getSize(ff.length())+") downloads="+ct)  );
                out.println(" AV Rating=:"+avRating+" No of users rated this item=:"+noOfUsers);
                this.writeRatingStars(out, ff, rating);
            }
            while(true){
                String l=br.readLine();
                if(l==null){
                    break;
                }
                out.println(l);
            }
            out.close();
        }
    
    
        /**
         * Create Leftframe and search for computers in LAN.
         * @throws FileNotFoundException if Leftframe.html is missing.
         * @throws IOException if Input output error occur.
         */
        protected void createLeftFrame(String req,PrintStream out) throws FileNotFoundException, IOException{
            out.println("HTTP/1.1 200 OK");
            out.println("content-type: text/html");
            out.println();
            BufferedReader br=new BufferedReader(new InputStreamReader(new ReadFile(req).getInputStream()));
            while(true){
                String l=br.readLine();
                if(l.indexOf("<%%>")!=-1){
                    break;
                }
                out.println(l);
            }
            FindLANShared f=new FindLANShared();
            
            //StringBuffer sb=new StringBuffer();
            ArrayList<NetFiles> n=f.getAllUsers();
           // System.out.println("nnnnn="+n);
            StringBuilder v=new StringBuilder();
            for(int i=0;i<n.size();i++){
                NetFiles fn= n.get(i);
                File  abc[] = fn.getFiles();
                StringBuffer sb=new StringBuffer();
                for (int j=0;j<abc.length;j++){
                //out.println("<a href='/getFolder?q="+n.get(i).getAdd() +"/'target='right'>"+n.get(i).getAdd().getHostName()+"</a>");
                //out.println("function myFunc"+ j +"() {document.getElementById('rightreplace').innerHTML="+"'"+abc[j]+"'"+";}");
                //out.println("function myFunc"+ j +"() {var divTag = document.createElement('div'); divTag.id = 'division"+i+"';divTag.setAttribute('align', 'center'); divTag.style.margin = '0px auto';divTag.className = 'dynamicDiv"+i+"';divTag.innerHTML ="+"'"+abc[j]+"'"+"document.body.appendChild(divTag);");
                    String e="/";
                    String a= abc[j].getAbsolutePath().substring(2).replace("\\", e);
                    if(a.contains("$")){//Restrict admin access 
                    	continue;
                    }
                    if(a.toLowerCase().contains("users")){
                    	continue;
                    }
                    a=URLEncoder.encode(a,"UTF-8");
                    
                    String st = "<a target =mid href=/midframe.html?//"+a +"> "+abc[j].getName()+"</a><br>";
                    sb.append(st);
                    v.append("//");
                    v.append(a);
                    v.append(":");
                
                }
                 
                //out.println(" function ShowHide"+i+"() {var pTag = document.createElement('p');pTag.id='para"+i+"';pTag.innerHTML = '"+sb.toString()+"';document.getElementById('rightreplace').appendChild(pTag); if(document.getElementById('rightreplace').style.display == 'none'){document.getElementById('rightreplace').style.display='block';}else{document.getElementById('rightreplace').style.display = 'none';}} ");
                 
                 
                 out.println(" function ShowHide"+i+"() {if(document.getElementById('rightreplace"+i+"').style.display == 'none'){document.getElementById('rightreplace"+i+"').style.display='block';}else{document.getElementById('rightreplace"+i+"').style.display = 'none';}} ");
                 out.println(" function callF"+i+"() {var pTag = document.createElement('p');pTag.id='para"+i+"';pTag.innerHTML = '"+sb.toString()+"';document.getElementById('rightreplace"+i+"').appendChild(pTag);} ");
                
                 
                
                
                /* Old
                *   out.println(" function ShowHide"+i+"() {var pTag = document.createElement('p');pTag.id='para"+i+"';pTag.innerHTML = '"+sb.toString()+"';document.getElementById('rightreplace').appendChild(pTag);} ");
               */
                }
             out.println(" function loadF() {");
             for(int i=0;i<n.size();i++){
                 out.println("callF"+i+"();");
             }
             out.println(" }");
             out.println("var data=\""+v.toString()+"\";");
           //out.println(" function loadF() {for (i=0;i<"+n.size()+";i++){callF"+i+"();}}");
            while(true){
                String l=br.readLine();
                if(l.indexOf("<%%>")!=-1){
                    break;
                }
                out.println(l);
            }
             
            
               
                out.println("onload ='javascript:loadF()'");
                
            
            
            while(true){
                String l=br.readLine();
                if(l.indexOf("<%%>")!=-1){
                    break;
                }
                out.println(l);
            }
            
            
            
            
            for(int i=0;i<n.size();i++){
                //out.println("<a onclick ='javascript:ShowHide"+i+"("+divid+")' href='javascript:;'>"+n.get(i).getAdd().getHostName()+"</a>");
                out.println("<a onclick ='javascript:ShowHide"+i+"()' href='javascript:;'>"+n.get(i).getAdd().getHostName()+"</a> <br>");
                out.println("<div class='p"+i+"' id='rightreplace"+i+"' name='rightreplace"+i+"' style='DISPLAY: none'></div>");
            }
          
            while(true){
                String l=br.readLine();
                if(l==null){
                    break;
                }
                out.println(l);
            }
            out.close();
            
        }
	
        
       /**
         * replace c1 with c2
         * @return new String.  
         */
        private String replaceAll(String s,char c1,char c2){
            StringBuilder sb=new StringBuilder(s.length());
            for(int i=0;i<s.length();i++){
                if(s.charAt(i)!=c1){
                    sb.append(s.charAt(i));
                }
                else{
                    sb.append(c2);
                }
            }
            return sb.toString();
        }
        
        /**
         * Create Vlc link in vlc.html
         */
        protected void createVLCLink(String req,PrintStream out) throws FileNotFoundException, IOException{
            WriteLog.println("creating vlc link:"+req);
            out.println("HTTP/1.1 200 OK");
            out.println("Cache-Control: no-cache");
            out.println("content-type: text/html");
            out.println();
            BufferedReader br=new BufferedReader(new InputStreamReader(new ReadFile("/vlc.html").getInputStream()));
            while(true){
                String l=br.readLine();
                if(l.indexOf("<%%>")!=-1){
                    break;
                }
                out.println(l);
            }
            char bs=(char)(92);
            req=replaceAll(req,bs,'/');
            
            out.println("target=\"smb:"+req.substring(0,req.length()-1) +"\" />");
            database.notifyAllSystems(new File(req));
            try{
            	File f=new File(req);
            	FileInfo vv=database.getFile(f);
            	double rating=vv.getRating();
            	double avRating=vv.getAvRating();
            	long noOfUsers=vv.getNoOfUser();
            	out.println("<p>Views="+vv.getDownloads()+"</p>");
            	String link="midframe.html?file::vlc//"+f.getAbsolutePath().substring(2)+"/";
            	//System.out.println(link);
            	out.println(" AV Rating=:"+avRating+" No of users rated this item=:"+noOfUsers);
            	out.println("<a href=\""+link+"\">download</a>");
            	this.writeRatingStars(out,f, rating);
            }catch(Exception e){
            	
            }
            while(true){
                String l=br.readLine();
                if(l==null){
                    break;
                }
                out.println(l);
            }
           
            out.close();
             
                    
        }
        
        
        /**
         * Request related to execution of files 
         * Like video audio and other files 
         * @throws FileNotFoundException
         * @throws IOException
         */
        protected void exploreFile(String req,PrintStream out) throws FileNotFoundException, IOException{
            boolean isVlc=false;
        	if(req.startsWith("vlc")){
            	req=req.substring(3);
            	isVlc=true;
            }
            ReadFile rf=new ReadFile(new File(req));
            String ct=rf.getContentType();
            
            if(!isVlc&&(ct.contains("AUDIO")||ct.contains("VIDEO"))){
                createVLCLink(req,out);
                rf.getInputStream().close();
                return ;
            }
            
            //WriteLog.println("file reading....."+rf.getContentType());
            out.println("HTTP/1.0 200 OK");
            out.println("content-type: "+ct);
            out.println("Content-disposition: filename="+rf.getFile().getName());
            out.println("Content-length: "+rf.getSize());
            out.println();
            //System.out.println("reading file... file read");
            try{
            	while(true){
                //make it efficient by reading byte array and using queue.
            		byte b[]=new byte[8000];
                
            		int ab=rf.readByteArray(b);
               
            		if(ab==b.length){
            			out.write(b);
            		}
            		else{
            			if(ab==-1){
            				//System.out.println("completed");
            				break;
            			}
            			else{
            				//System.out.println("par==="+ab);
            				byte bb[]=new byte[ab];
            				System.arraycopy(b, 0, bb, 0, ab);
            				out.write(bb);
                        
            			}
            		}
                
            	}
            	database.notifyAllSystems(rf.getFile());
             }catch(IOException e){//catch for while...
                // System.out.println("closed....");
             }
             finally{
                    out.close();
                   // rf.close();
             }
           // System.out.println("reading comp");
            out.close();
            
            System.gc();
            //out.println("explorering...... file");
           
        }
        /**
         * Writes rating stars in output stream.
         */
        private void writeRatingStars(PrintStream out,File f,double rating){ 
            StringBuilder sb=new StringBuilder();
            sb.append("<div class='starRate'>");
            sb.append("<div><b></b></div>");
            sb.append("<ul>");
            int v=(int )rating;
            v=rating-v<.5?v:v+1;
            v=6-v;
            for(int i=0;i<5;i++){
                String href="midframe.html?rating="+f.getAbsolutePath()+":"+(5-i);
                if((i+1)!=v){
                    
                    sb.append("<li><a target='hidden' href=\"").append(href).append("\"></a></li>");
                }
                else{
                    sb.append("<li><a target='hidden' href=\"").append(href).append("\"><b></b></a></li>");
                }
            }
            sb.append("</ul>");
            out.println(sb.toString());
        }
        /**
         * 
         * Sets rating of file
         * file and rating separated by ":"
         * @throws IOException 
         */
        protected void setRating(String req) throws IOException{
        	
        	String s[]=req.split(":");
        	File f=new File(s[0]);
        	int r=Integer.parseInt(s[1]);
        	database.notifyRating(f, r);
        	//database.setRating(f, r);
        }
        
        /**
         * all request related to midframe.html redirected hare...
         * @param req request.
         * @param out Output stream.
         * @throws FileNotFoundException
         * @throws IOException
         * @throws Exception 
         */
        protected void openFolder(String req,PrintStream out) throws FileNotFoundException, IOException,Exception{
            req=URLDecoder.decode(req,"UTF-8");
            if(req.indexOf("search=")==0){//(search="req")
                search(req,out);
                return;
            }
            
            if(req.startsWith("rating=")){//to set rating
            	WriteLog.println("rating==:"+req);
            	this.setRating(req.substring(7));
            	out.close();
            	return;
            }
            if(!req.contains("//")){
                throw new Exception("wrong request");
            }
            WriteLog.println("requested folder or file="+req);
            if(req.contains("file::")){
                req=URLDecoder.decode(req,"UTF-8");
               exploreFile(req.substring(6),out);
               return;
            }
            WriteLog.println("requested folder="+req);
            File f=new File(req);
            database.notifyAllSystems(f);
            File list[]=f.listFiles();
            CompareFiles.sort(list, database);
            out.println("HTTP/1.1 200 OK");
            out.println("content-type: text/html");
            out.println();
            req=URLDecoder.decode(req,"UTF-8");
            BufferedReader br=new BufferedReader(new InputStreamReader(new ReadFile("/midframe.html/").getInputStream()));
            WriteLog.println("req==="+req+",file---");
            while(true){
                String l=br.readLine();
                if(l.indexOf("<%%>")!=-1){
                    break;
                }
                out.println(l);
            }
            
            out.println("<h2 id='dirpath' name='dirpath'>"+f.getAbsolutePath()+"</h2>");
            
            for(int i=0;i<list.length;i++){
                String link=list[i].isDirectory()?"\"midframe.html?//"+list[i].getAbsolutePath().substring(2) +"/\"":"\"midframe.html?file:://"+list[i].getAbsolutePath().substring(2)+"/\"";
               // String v="<li><a href="+link+">"+list[i].getName()+"</a> <span class='info'>("+(list[i].isDirectory()?"Folder )":(list[i].length()/(1024.0*1024))+" MB ) downloads=:"+database.getFile(list[i]).getDownloads()+"</li>")  ;
                long ct=0;
                double rating=0;
                double avRating=0;
                long noOfUsers=0;
                try{
                	FileInfo vv=database.getFile(list[i]);
                	ct=vv.getDownloads();
                	rating=vv.getRating();
                	avRating=vv.getAvRating();
                	noOfUsers=vv.getNoOfUser();
                }
                catch(FileNotFoundException e){
                	
                }
                out.print("<li><a href="+link+">"+list[i].getName()+"</a> <span class='info'>("+(list[i].isDirectory()?"Folder ) Opened="+ct:(getSize(list[i].length()))+" ) downloads="+ct)  );
                out.println(" AV Rating=:"+avRating+" No of users rated this item=:"+noOfUsers);
                this.writeRatingStars(out, list[i], rating);
                //out.println("<br><p>"+avRating+"</p>");
            }
            while(true){
                String l=br.readLine();
                if(l==null){
                    break;
                }
                out.println(l);
            }
            out.close();
              
        }
        /**
         * convert size in byte into String
         * @param size
         * @return String representation of size
         */
        protected String getSize(long size){
        	return String.format("%.2f",size/(1024.0*1024))+" MB";
        }
        
        /**
         * Search request c:req means complete keyword search p:req means partial keyword search 
         * 
         * @param req example="search=query&folder=\\192.168.1.164\music&allfolders=192.168.1.164%2Fmusic:192.168.1.164%2FUsers:192.168.1.164%2FC%24:192.168.1.164%2FD%24:192.168.1.164%2Fmovies:192.168.1.164%2FIPC%24:192.168.1.164%2Fprint%24:192.168.1.164%2FAngry+Birds:192.168.1.164%2FADMIN%24:192.168.1.164%2Fsoft:&selid=current&searchtype=partial"
         * @param out
         * @throws Exception
         */
        protected void search(String req,PrintStream out) throws Exception{
        	
        	long t=System.currentTimeMillis();
        	//System.out.println("Search req=:"+req);
        	req=URLDecoder.decode(req,"UTF-8");
        	req=this.replaceAll(req, (char)92, '/');
        	String sr[]=req.split("&");
        	//System.out.println(Arrays.toString(sr));
        	
        	
        	LinkedList<File> l = null;
        	
        	if(sr[4].equals("searchtype=partial")){
        		if(sr[3].equals("selid=current")){
        			l=sk.search(sr[1].substring(7)+":"+sr[0].substring(7));
        		}
        		else if(sr[3].equals("selid=all")){
        			//System.out.println("vvvvv"+sr[2]+","+sr[0]);
        			l=sk.search(sr[2].substring(11)+sr[0].substring(7));
        		}
        	}
        	else{
        		if(sr[3].equals("selid=current")){
        			l=sk.searchComp(sr[1].substring(7)+":"+sr[0].substring(7));
        		}
        		else if(sr[3].equals("selid=all")){
        			//System.out.println("vvvvv"+sr[2]+","+sr[0]);
        			l=sk.searchComp(sr[2].substring(11)+sr[0].substring(7));
        		}
        	}
        	WriteLog.println("Searching time="+(System.currentTimeMillis()-t));
        	this.showSearchResultList(l, out);
        	
        	out.close();
        }
        
        /**
         *root/recommendation redirected Hare.
         * @param req Req String.
         * @param out OutputStream
         * @throws Exception 
         */
        protected void recommendation(String req,PrintStream out) throws Exception{
        	//System.out.println("Recom"+req);
        	List<File> l;
        	if(req.equals("all")){
        		l=rec.getRecommendation();
        	}
        	else if(req.equals("files")){
        		l=rec.getRecommendedFiles();
        	}
        	else if(req.equals("folders")){
        		l=rec.getRecommendedFolders();
        	}
        	else if(req.equals("videos")){
        		l=rec.getRecommendedVideos();
        	}
        	else if(req.equals("ebooks")){
        		l=rec.getRecommendedEbooks();
        	}
        	else if(req.equals("music")){
        		l=rec.getRecommendedMusic();
        	}
        	else{
        		throw new Exception("wrong request recommendation");
        	}
        	this.showSearchResultList(l, out);
        } 
        
        
        
        @Override
        public void doGet(ArrayList<String> header, OutputStream os) {
        		//System.out.println("GET==:"+header);
                PrintStream out=new PrintStream(os);
                String s[]=header.get(0).split(" ");
                try {
                     String v=""+s[1];
                     v=URLDecoder.decode(v,"UTF-8");
                     if(!isValidURL(v)){
                         sendRedirect(out,"error.html");
                         return ;
                     }
                     if(v.contains("recommendation")){
                    	 this.recommendation(v.substring(16), out);
                    	 return;
                     }
                     if(v.indexOf("/midframe.html")!=-1){          
                        openFolder(v.substring(v.indexOf('?')+1),out);
                        return ;
                     }
                       //create dynamic left frame
                     if(v.equals("/leftframe.html")){
                        createLeftFrame(s[1],out);
                        return ;
                      }
                    // System.out.println("reading file....");  
                     ReadFile rf=new ReadFile(s[1]);
                       
                     out.println("HTTP/1.1 200 OK");
			//write 
                     out.println("content-type: "+rf.getContentType());
                     out.println("Content-length: "+rf.getSize());
                     out.println("Content-disposition: filename="+rf.getFile().getName());
                     out.println();   
                     while(true){
				//make it efficient by reading byte array and using queue.
		
			byte b[]=new byte[8000];
			int ab=rf.readByteArray(b);
			if(ab==b.length){
				os.write(b);
			}
			else{
				if(ab==-1)
					break;
				else{
					byte bb[]=new byte[ab];
                    System.arraycopy(b, 0, bb, 0, ab);
					os.write(bb);
				}
			}
			
                    }
                    //rf.close(); 
		} catch (Exception e) {
			
                    WriteLog.println("error="+e);
                    sendRedirect(out,"error.html");
		}
                
               
                
                //System.out.print("reading file comp");
		System.gc();
		out.close();
		
		
	}
        /**
         * send redirect request.
         * @param out OutputStream
         * @param url Redirect to this URL
         */
        public void sendRedirect(PrintStream out,String url){
            out.println("HTTP/1.1 307 Temporary Redirect");
            out.println("Location: "+url);
            out.println("Content-Type: text/html");
            out.println(); 
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Moved</title>");
            out.println("</head><body><h1>Moved</h1><p>This page has moved to <a href='"+url+"'>This link/</a>.</p></body></html>");
        }
        
	@Override
	public void doPost(ArrayList<String> header, OutputStream out) {
		
		
	}

}
