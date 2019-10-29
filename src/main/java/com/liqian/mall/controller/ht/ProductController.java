package com.liqian.mall.controller.ht;

import com.alibaba.druid.util.StringUtils;
import com.liqian.mall.common.Const;
import com.liqian.mall.common.Result;
import com.liqian.mall.config.FtpConfig;
import com.liqian.mall.entity.Product;
import com.liqian.mall.entity.User;
import com.liqian.mall.error.BusinessException;
import com.liqian.mall.error.EmError;
import com.liqian.mall.service.IFileService;
import com.liqian.mall.service.IProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 后台商品接口
 *
 * @author liqian
 */
@RestController
@RequestMapping("ht_product")
public class ProductController {

    @Resource
    private IProductService iProductService;

    @Resource
    private IFileService iFileService;

    @Resource
    private FtpConfig ftpConfig;

    @Resource
    private HttpSession session;

    @GetMapping("addOrUpdate")
    public Result addOrUpdateProduct(Product product) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        if (user.getRole() != Const.Role.ROLE_ADMIN) {
            throw new BusinessException(EmError.NOT_PERMISSION);
        }
        return iProductService.addOrUpdateProduct(product);
    }

    @GetMapping("setStatus")
    public Result setProductStatus(Integer productId, Integer status) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        if (user.getRole() != Const.Role.ROLE_ADMIN) {
            throw new BusinessException(EmError.NOT_PERMISSION);
        }
        return iProductService.setProductStatus(productId, status);
    }

    @GetMapping("detail")
    public Result getProductDetail(Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        if (user.getRole() != Const.Role.ROLE_ADMIN) {
            throw new BusinessException(EmError.NOT_PERMISSION);
        }
        return iProductService.getHtProductDetail(productId);
    }

    @GetMapping("list")
    public Result listProduct(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                              @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        if (user.getRole() != Const.Role.ROLE_ADMIN) {
            throw new BusinessException(EmError.NOT_PERMISSION);
        }
        return iProductService.listProduct(pageNum, pageSize);
    }

    @GetMapping("search")
    public Result searchProduct(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                String productName, Integer productId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        if (user.getRole() != Const.Role.ROLE_ADMIN) {
            throw new BusinessException(EmError.NOT_PERMISSION);
        }
        return iProductService.searchProduct(pageNum, pageSize, productName, productId);
    }

    @GetMapping("upload")
    public Result upload(MultipartFile multipartFile) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            throw new BusinessException(EmError.USER_NOT_LOGIN);
        }
        if (user.getRole() != Const.Role.ROLE_ADMIN) {
            throw new BusinessException(EmError.NOT_PERMISSION);
        }
        String path = session.getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(path, multipartFile);
        String url = ftpConfig.getImageHost() + targetFileName;
        Map<String, String> map = new HashMap<>(8);
        map.put("uri", targetFileName);
        map.put("url", url);
        return Result.createBySuccess(map);
    }

    @GetMapping("richTextUpload")
    public Map<String, Object> richTextUpload(MultipartFile multipartFile, HttpServletResponse response) {
        Map<String, Object> returnMap = new HashMap<>(8);
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            returnMap.put("success", false);
            returnMap.put("msg", "请登录管理员");
            return returnMap;
        }
        if (user.getRole() != Const.Role.ROLE_ADMIN) {
            returnMap.put("success", false);
            returnMap.put("msg", "无权访问");
            return returnMap;
        }
        String path = session.getServletContext().getRealPath("upload");
        String targetFileName = iFileService.upload(path, multipartFile);
        if (StringUtils.isEmpty(targetFileName)) {
            returnMap.put("success", false);
            returnMap.put("msg", "上传失败");
            return returnMap;
        } else {
            //富文本返回值所要求的格式，使用simditor
            String url = ftpConfig.getImageHost() + targetFileName;
            returnMap.put("success", true);
            returnMap.put("msg", "上传成功");
            returnMap.put("file_path", url);
            response.setHeader("Access-Control-Allow-Headers", "X-File-Name");
            return returnMap;
        }
    }
}
