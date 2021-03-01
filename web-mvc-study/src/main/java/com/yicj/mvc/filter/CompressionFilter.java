package com.yicj.mvc.filter;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.zip.GZIPOutputStream;

//@Component
public class CompressionFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        boolean compress = false ;
        Enumeration headers = request.getHeaders("Accept-Encoding");
        while(headers.hasMoreElements()){
            String value = (String) headers.nextElement();
            if(value.contains("gzip")){
                compress =  true ;
            }
        }
        if(compress){ // 如果浏览器支持则压缩
            response.addHeader("Content-Encoding","gzip");
            CompressionResponse compressionResponse = new CompressionResponse(response);
            chain.doFilter(request, compressionResponse );
            compressionResponse.close();
        }else{//如果浏览器不支持则不压缩
            chain.doFilter(request, response);
        }
    }

    class CompressionResponse extends HttpServletResponseWrapper {
        protected HttpServletResponse response ;
        private ServletOutputStream out ;
        private CompressedStream  compressedOut ;
        private PrintWriter writer ;
        protected int contentLength ;
        public CompressionResponse(HttpServletResponse response) throws IOException {
            super (response);
            this.response = response;
            compressedOut = new CompressedStream(response.getOutputStream());
        }
        public void setContentLength(int len) {
            contentLength = len;
        }
        public ServletOutputStream getOutputStream()throws IOException {
            if(null ==  out){
                if(null !=  writer) {
                    throw new IllegalStateException( "getWriter() has already been called on this response." );
                }
                out = compressedOut ;
            }
            return out ;
        }
        public PrintWriter getWriter()throws IOException{
            if(null == writer){
                if(null !=  out){
                    throw new IllegalStateException("getOutputStream() has already been called on this response." );
                }
                writer = new PrintWriter(compressedOut);
            }
            return writer ;
        }
        public void flushBuffer(){
            try{
                if( writer != null ){
                    writer.flush();
                }else if(out != null){
                    out.flush();
                }
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        public void reset() {
            super.reset();
            try{
                compressedOut.reset();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }
        public void resetBuffer(){
            super.resetBuffer();
            try{
                compressedOut.reset();
            } catch(IOException e){
                throw new RuntimeException(e);
            }
        }
        public void close()throws IOException {
            compressedOut.close();
        }
    }

    class CompressedStream extends ServletOutputStream {
        private ServletOutputStream out ;
        private GZIPOutputStream gzip ;
        public CompressedStream(ServletOutputStream out) throws IOException {
            this.out = out;
            reset();
        }
        public void close() throws IOException {
            gzip.close();
        }
        public void flush() throws IOException {
            gzip.flush();
        }
        public void write(byte [] b)  throws   IOException {
            write(b, 0, b. length );
        }
        public void  write(byte [] b, int off, int len)throws IOException {
            gzip.write(b, off, len);
        }
        public void write(int b) throws IOException {
            gzip.write(b);
        }
        public void reset() throws IOException {
            gzip = new GZIPOutputStream(out);
        }
        @Override
        public boolean isReady() {
            return out.isReady();
        }
        @Override
        public void setWriteListener(WriteListener writeListener) {
            out.setWriteListener(writeListener);
        }
    }
}
