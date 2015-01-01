package proto.ms.ui;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by civerson on 12/26/14.
 */
@Slf4j
@Controller
@RequestMapping({"/files", "/"})
public class FilesController {

    @Autowired
    private RemoteFileService remoteFileService;

    @RequestMapping
    public String getFiles(ModelMap model) {

        long start = System.currentTimeMillis();
        model.addAttribute("remoteFiles", remoteFileService.getFiles());
        log.info("get files took " + (System.currentTimeMillis() - start));
        return "files";
    }

    @RequestMapping("/delete/{id}")
    public String delete(@PathVariable String id) {
        long start = System.currentTimeMillis();
        remoteFileService.delete(id);
        log.info("delete took " + (System.currentTimeMillis() - start));
        return "redirect:/files";
    }

    @RequestMapping("/download/{id}")
    public void downloadFile(@PathVariable String id, HttpServletResponse response, ModelMap model) {
        long start = System.currentTimeMillis();
        remoteFileService.downloadFile(id, response);
        log.info("download took " + (System.currentTimeMillis() - start));
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadFile(@RequestParam("theFile") MultipartFile file) {

        long start = System.currentTimeMillis();
        if (!file.isEmpty()) {
            String name = file.getOriginalFilename();
            try {
                remoteFileService.uploadFile(file);
                log.info("upload took " + (System.currentTimeMillis() - start));
                return "redirect:/files";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "The selected file was empty and could not be uploaded.";
        }
    }


}
