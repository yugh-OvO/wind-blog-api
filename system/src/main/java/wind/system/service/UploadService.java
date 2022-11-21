package wind.system.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wind.common.utils.StringUtils;

import java.io.IOException;
import java.io.InputStream;

/**
 * 文件上传 服务层
 *
 * @author Yu Gaoheng
 */
@RequiredArgsConstructor
@Service
public class UploadService {

    public String upload(MultipartFile file) {

        String endpoint = "";
        String accessKeyId = "";
        String accessKeySecret = "";
        String bucketName = "";
        String bucketUrl = "";

        // 取后缀
        String originalfileName = file.getOriginalFilename();
        System.out.println("originalfileName is " + originalfileName);

        String suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length());
        // 上传目标路径
        String objectName = "shiguangji/" + DateUtil.format(DateUtil.date(), "Y/m/d") + "/" + RandomUtil.randomString(12) + suffix;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            InputStream inputStream = null;
            try {
                inputStream = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, inputStream);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                + "a serious internal problem while trying to communicate with OSS, "
                + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return bucketUrl + "/" + objectName;
    }

}
