package jupiterpa.sitesviewer;

import jupiterpa.sitesviewer.filetool.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class Loader {
    public String getHomeLink() {
        ArrayList<String> homeLink = getInternalSource("homeLink.html");
        return listToString(homeLink);
    }

    public String getPage(String pageName) {
        ArrayList<String> pageParts = getPageParts(pageName);
        return itemsToString(pageParts);
    }

    public String getPageWithPathVariables(String pageName, String[] pathVariables) {
        System.out.println(pathVariables);

        ArrayList<String> pageParts = getPageParts(pageName);

        ArrayList<String> pathVariableLines = new ArrayList<String>();
        ArrayList<String> putPathVaribaleParts = getInternalSource("putPathVariable.html");
        for (String pathVariable : pathVariables) {
            pathVariableLines.add(putPathVaribaleParts.get(0) + pathVariable + putPathVaribaleParts.get(1));
        }
        String pathVariableLinesString = listToString(pathVariableLines);

        pageParts.add(1, pathVariableLinesString);
        return itemsToString(pageParts);
    }

    private ArrayList<String> getPageParts(String pageName) {
        String[] pageNameParts = pageName.split("/");
        String pagesPath = ".\\pages\\";
        for (int i = 0; i < pageNameParts.length-1; i++) {
            pagesPath += pageNameParts[i] + "\\";
        }
        pageName = pageNameParts[pageNameParts.length-1];

        JSONObject pageData = getJSONFromFile(pagesPath + pageName + ".json");

        String containerName = (String) pageData.get("container");
        JSONObject containerData = getJSONFromFile(".\\containers\\" + containerName);
        String containerTopName = (String) containerData.get("top");
        String containerTop = getFileAsString(".\\containers\\" + containerTopName);
        String containerBottomName = (String) containerData.get("bottom");
        String containerBottom = getFileAsString(".\\containers\\" + containerBottomName);

        String pageFileName = (String) pageData.get("content");
        ArrayList<String> pageList = new FileTool(pagesPath + pageFileName).getFile();
        pageList = enableBlocks(pageList);
        String page = listToString(pageList);

        String metaLines = "";
        Map<String, String> meta = (Map<String, String>) pageData.get("meta");
        for (String key : meta.keySet()) {
            String value = meta.get(key);
            ArrayList<String> metaParts = getInternalSource("setMeta.html");
            String metaLine = metaParts.get(0) + key + metaParts.get(1) + value + metaParts.get(2);
            metaLines = metaLine + "\n" + metaLines;
        }

        ArrayList<String> pageParts = new ArrayList<String>();
        pageParts.add(containerTop);
        pageParts.add(metaLines);
        pageParts.add(page);
        pageParts.add(containerBottom);

        return pageParts;
    }

    private ArrayList<String> enableBlocks(ArrayList<String> page) {
        for (int i = 0; i < page.size(); i++) {
            String line = page.get(i);
            try {
                if (line.substring(0, 2).equals("##")) {
                    if (!line.substring(2).split(":")[1].equals("{")) {
                        System.out.println(line);
                        ArrayList<String> blockList = new ArrayList<String>();
                        blockList.add(line);
                        page.set(i, getBlock(blockList));
                    } else {
                        System.out.println(i);
                        for (int j = i; j < page.size(); j++) {
                            String jLine = page.get(j);
                            System.out.println(j + jLine);
                            if (jLine.equals("}")) {
                                ArrayList<String> blockList = new ArrayList<String>();
                                for (int k = i; k <= j; k++) {
                                    blockList.add(page.get(k));
                                }
                                for (String blockLine : blockList) {
                                    System.out.println("blockLine:" + blockLine);
                                    page.remove(blockLine);
                                }
                                page.add(i, getBlock(blockList));
                            }
                        }
                    }
                }
            } catch (StringIndexOutOfBoundsException x) {
                break;
            }
        }
        return page;
    }

    public ArrayList<String> getInternalSource(String name) {
        return new FileTool(".\\internal\\" + name).getFile();
    }

    public String getRes(String resName) {
        return getFileAsString(".\\res\\" + resName);
    }

    public String getBlock(ArrayList<String> inputLines) {
        System.out.println(inputLines);
        String[] headingLine = inputLines.get(0).split(":");
        boolean multiBlocks = headingLine[1].equals("{");

        String blockFileName = headingLine[0].substring(2);
        ArrayList<String> blockFile = new FileTool(".\\blocks\\" + blockFileName + ".html").getFile();

        if (!multiBlocks) {
            return getBlockWithArgsString(blockFile, headingLine[1]);
        } else {
            ArrayList<String> argsLines = new ArrayList<>();
            for (int i = 1; i < inputLines.size()-1; i++) {
                argsLines.add(inputLines.get(i));
            }

            String blocks = "";
            for (String argsLine : argsLines) {
                blocks += getBlockWithArgsString(blockFile, argsLine);
            }
            return blocks;
        }
    }

    private String getBlockWithArgsString(ArrayList<String> blockFile, String argsLine) {
        String[] argsString = argsLine.split(",");
        Map<String, String> args = new HashMap<>();
        for (String argString : argsString) {
            String[] pair = argString.split("=");
            args.put(pair[0], pair[1]);
        }
        return getBlockWithArgs(blockFile, args);
    }

    private String getBlockWithArgs(ArrayList<String> blockFile, Map<String, String> args) {
        String blockString = itemsToString(blockFile);
        String[] segments = blockString.split("##");
        for (int i = 1; i < segments.length; i += 2) {
            String segment = segments[i];
            segment = args.get(segment);
            segments[i] = segment;
        }
        ArrayList<String> segmentsAsArrayList = new ArrayList<String>();
        for (String item : segments) {
            segmentsAsArrayList.add(item);
        }
        return itemsToString(segmentsAsArrayList);
    }

    public JSONArray getHierarchy() {
        JSONArray json = new JSONArray();
        File pagesDir = new File(".\\pages");
        for (File file : pagesDir.listFiles()) {
            if (file.getName().endsWith(".json")) json.add(createFileInHierarchyObject(file));
        }
        return json;
    }

    private JSONObject createFileInHierarchyObject(File file) {
        JSONObject jsonFile = getJSONFromFile(file.getAbsolutePath());
        JSONObject fileInHierarchy = new JSONObject();
        fileInHierarchy.put("path", file.getName());
        fileInHierarchy.put("name", jsonFile.get("hierarchyName"));
        String subpagesDirName = (String) jsonFile.get("subpages");
        if (subpagesDirName != null) {
            JSONArray subpagesArray = new JSONArray();
            String absolutePath = file.getParentFile().getAbsolutePath() + "\\" + subpagesDirName;
            File subpagesDir = new File(absolutePath);
            try {
                for (File subpageFile : subpagesDir.listFiles()) {
                    if (subpageFile.getName().endsWith(".json")) subpagesArray.add(createFileInHierarchyObject(subpageFile));
                }
                fileInHierarchy.put("subpages", subpagesArray);
            } catch (NullPointerException x) {
                fileInHierarchy.put("subpages", null);
            }
        } else {
            fileInHierarchy.put("subpages", null);
        }
        return fileInHierarchy;
    }

    public JSONObject getJSONFromFile(String fileName) {
        FileTool file = new FileTool(fileName);
        String fileString = itemsToString(file.getFile());
        return (JSONObject) JSONValue.parse(fileString);
    }

    public String getFileAsString(String fileName) {
        ArrayList<String> file = new FileTool(fileName).getFile();
        return listToString(file);
    }

    private String itemsToString(ArrayList<String> list) {
        String str = "";
        for (String item : list) {
            str += item;
        }
        return str;
    }

    private String listToString(ArrayList<String> list) {
        String str = "";
        if (list.size() == 0) return "";
        str = list.get(0);
        if (list.size() == 1) return str;
        for (int i = 1; i < list.size(); i++) {
            str += "\n" + list.get(i);
        }
        return str;
    }

    public String[] splitURL(HttpServletRequest request) {
        String[] path = request.getRequestURL().toString().substring(8).split("/");
        path = Arrays.copyOfRange(path, 1, path.length);
        return path;
    }

    public String listToPath(String[] list) {
        int length = list.length;
        switch (length) {
            case 0: return "";
            case 1: return list[0];
        }
        String path = list[0];
        for (int i = 1; i < list.length; i++) {
            path += "/" + list[i];
        }
        return path;
    }
}