package jupiterpa.sitesviewer;

import com.amazonaws.util.IOUtils;
import org.json.simple.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Arrays;

@RestController
@RequestMapping(path = "")
public class Controller {
    @Autowired
    Loader loader;

    @GetMapping("/hello")
    public String hello (HttpServletRequest request) {
        return loader.getPage("backgrounds/overview");
    }

    @GetMapping("")
    public String getHomeLink() {
        return loader.getHomeLink();
    }

    @GetMapping("/pages/**")
    public String getPage(HttpServletRequest request) {
        String[] urlParts = loader.splitURL(request);
        int pathvBeginIndex = 0;
        for (int i = 0; i < urlParts.length; i++) {
            if (urlParts[i].equals("pv")) {
                pathvBeginIndex = i+1;
                break;
            }
        }
        if (pathvBeginIndex != 0) {
            String[] pageNameParts = Arrays.copyOfRange(urlParts, 1, pathvBeginIndex-1);
            String pageName = loader.listToPath(pageNameParts);
            String[] pathv = Arrays.copyOfRange(urlParts, pathvBeginIndex, urlParts.length);
            return loader.getPageWithPathVariables(pageName, pathv);
        } else {
            String[] pageNameParts = Arrays.copyOfRange(urlParts, 1, urlParts.length);
            String pageName = loader.listToPath(pageNameParts);
            return loader.getPage(pageName);
        }
    }

    /*
    @GetMapping("/pages/**")
    public String getPage(HttpServletRequest request) {
        String[] path = request.getRequestURL().toString().substring(8).split("/");
        path = Arrays.copyOfRange(path, 2, path.length);
        return loader.getPage(loader.listToPath(path));
    }

    @GetMapping("/pages/*pv**")
    public String getPageWithPathVariables(HttpServletRequest request) {
        String path = request.getRequestURL().toString();
        String[] pathParts = path.split("/pv/");
        String[] pageNameParts = pathParts[0].split("/");
        pageNameParts = Arrays.copyOfRange(pageNameParts, 2, pageNameParts.length);
        String[] pathVariables = pathParts[1].split("/");
        return loader.getPageWithPathVariables(loader.listToPath(pageNameParts), pathVariables);
    }
    */

    @GetMapping("/hierarchy")
    public JSONArray getHierarchy() {
        return loader.getHierarchy();
    }

    @GetMapping("/res/{resName}")
    public Object getRes(@PathVariable String resName) {
        return loader.getRes(resName);
    }

    @GetMapping("/pic/{resName}")
    public ResponseEntity<byte[]> getPicture(@PathVariable String resName) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        InputStream in = new FileInputStream(new File(".\\res\\" + resName));
        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
        byte[] res = IOUtils.toByteArray(in);
        return new ResponseEntity<>(res, headers, HttpStatus.OK);
    }
}
