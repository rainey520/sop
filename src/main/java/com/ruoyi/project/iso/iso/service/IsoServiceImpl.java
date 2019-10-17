package com.ruoyi.project.iso.iso.service;

import com.ruoyi.common.constant.DevConstants;
import com.ruoyi.common.constant.FileConstants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.constant.WorkConstants;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.support.Convert;
import com.ruoyi.common.utils.CodeUtils;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.jwt.JwtUtil;
import com.ruoyi.project.code.activeCode.domain.ActiveCode;
import com.ruoyi.project.code.activeCode.domain.ApiActiveCode;
import com.ruoyi.project.code.activeCode.mapper.ActiveCodeMapper;
import com.ruoyi.project.device.devCompany.domain.DevCompany;
import com.ruoyi.project.device.devCompany.mapper.DevCompanyMapper;
import com.ruoyi.project.device.devList.domain.DevList;
import com.ruoyi.project.device.devList.mapper.DevListMapper;
import com.ruoyi.project.iso.iso.domain.Iso;
import com.ruoyi.project.iso.iso.mapper.IsoMapper;
import com.ruoyi.project.iso.sopLine.domain.SopLine;
import com.ruoyi.project.iso.sopLine.mapper.SopLineMapper;
import com.ruoyi.project.page.pageInfo.domain.SopApi;
import com.ruoyi.project.production.devWorkOrder.domain.DevWorkOrder;
import com.ruoyi.project.production.devWorkOrder.mapper.DevWorkOrderMapper;
import com.ruoyi.project.production.productionLine.domain.ProductionLine;
import com.ruoyi.project.production.productionLine.mapper.ProductionLineMapper;
import com.ruoyi.project.production.workstation.domain.Workstation;
import com.ruoyi.project.production.workstation.mapper.WorkstationMapper;
import com.ruoyi.project.system.user.domain.User;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

/**
 * 文件管理 服务层实现
 *
 * @author sj
 * @date 2019-06-13
 */
@Service("isoservice")
public class IsoServiceImpl implements IIsoService {
    @Autowired
    private IsoMapper isoMapper;

    @Autowired
    private DevCompanyMapper companyMapper;

    @Autowired
    private WorkstationMapper workstationMapper;

    @Autowired
    private ProductionLineMapper productionLineMapper;

    @Autowired
    private DevWorkOrderMapper devWorkOrderMapper;

    @Autowired
    private SopLineMapper sopLineMapper;

    @Autowired
    private DevListMapper devListMapper;

    @Autowired
    private DevCompanyMapper devCompanyMapper;

    @Autowired
    private ActiveCodeMapper activeCodeMapper;

    @Value("${file.iso}")
    private String isoFileUrl;

    /**
     * 查询文件管理信息
     *
     * @param id 文件管理ID
     * @return 文件管理信息
     */
    @Override
    public Iso selectIsoById(Integer id) {
        return isoMapper.selectIsoById(id);
    }

    /**
     * 查询文件管理列表
     *
     * @param iso 文件管理信息
     * @return 文件管理集合
     */
    @Override
    public List<Iso> selectIsoList(Iso iso) {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        if (user == null) {
            return Collections.emptyList();
        }
        iso.setCompanyId(user.getCompanyId());
        return isoMapper.selectIsoList(iso);
    }

    /**
     * 新增文件管理
     *
     * @param iso 文件管理信息
     * @return 结果
     */
    @Override
    public int insertIso(Iso iso) {
        User tokenUser = JwtUtil.getTokenUser(ServletUtils.getRequest());
        if (tokenUser == null) {
            return 0;
        }
        Iso uniqueIso = isoMapper.selectIsoByFolderNameUnique(iso.getiType(), iso.getParentId(), iso.getcName(), tokenUser.getCompanyId());
        if (StringUtils.isNotNull(uniqueIso)) {
            throw new BusinessException("文件名称重复，请重新输入");
        }
        //判断SOP文件夹是否存在
        String sopPath = RuoYiConfig.getProfile() + "sop" + tokenUser.getCompanyId();
        File file = new File(sopPath);
        if (!file.exists()) {
            //不存在创建对应文件夹
            file.mkdir();
        }
        String randomPath = CodeUtils.getRandomChar() + CodeUtils.getRandom(); // 随机路径
        // 校验随机生成的路径是否重复
        Iso randomIso = isoMapper.selectIsoByRandomName(randomPath);
        if (StringUtils.isNotNull(randomIso)) { // 存在相同的文件名
            // 重新生成随机英文名
            randomPath = CodeUtils.getRandomChar() + CodeUtils.getRandom();
        }
        String filePath = RuoYiConfig.getProfile() + "sop" + tokenUser.getCompanyId() + "/" + randomPath;
        File myPath = new File(filePath);
        if (!myPath.exists()) {//若此目录不存在，则创建之
            myPath.mkdir();
        }
        iso.seteName(randomPath);
        iso.setDiskPath("sop" + tokenUser.getCompanyId() + "/" + randomPath);
        iso.setDisk(filePath);
        iso.setcId(tokenUser.getUserId().intValue());
        iso.setcTime(new Date());
        iso.setCompanyId(tokenUser.getCompanyId());
        iso.setGrParentId(0);
        iso.setIsoId(null);
        return isoMapper.insertIso(iso);
    }

    /**
     * 修改文件管理
     *
     * @param iso 文件管理信息
     * @return 结果
     */
    @Override
    public int updateIso(Iso iso, HttpServletRequest request) throws IOException {
        User user = JwtUtil.getTokenUser(request);
        if (user == null) {
            return 0;
        }
        Iso isoData = isoMapper.selectIsoById(iso.getIsoId());
        Iso isoUnique = null;
        if (StringUtils.isNotNull(isoData)) {
            // 获取文件的类型
            Integer iType = iso.getiType();
            if (FileConstants.ITYPE_FILE.equals(iType)) {
                // 判断文件名是否重复
                isoUnique = isoMapper.selectIsoByEName(isoData.getDiskPath(), iso.geteName());
                if (StringUtils.isNotNull(isoUnique)) {
                    if (!isoUnique.getIsoId().equals(iso.getIsoId())) {
                        throw new BusinessException("文件名称重复，请重新输入");
                    }
                    if (iso.geteName().equals(isoUnique.geteName())) {
                        return isoMapper.updateIso(iso);
                    }
                } else {
                    // 获取旧文件信息
                    File file = new File(isoData.getDisk() + "/" + isoData.getcName());
                    if (file.exists()) { // 文件存在
                        String suffixName = file.getName();
                        suffixName = suffixName.substring(suffixName.lastIndexOf("."), suffixName.length()); // 文件名后缀
                        // 创建新的文件
                        File newFile = new File(isoData.getDisk() + "/" + iso.geteName() + suffixName);
                        FileUtils.copyFile(file, newFile);
                        file.delete();
                        iso.setcName(iso.geteName() + suffixName);
                        iso.setPath(isoFileUrl + isoData.getDiskPath() + "/" + iso.geteName() + suffixName);
                        iso.seteName(iso.geteName());
                    }
                }
            } else { // 文件夹类型
                isoUnique = isoMapper.selectIsoByFolderNameUnique(FileConstants.ITYPE_FOLDER, isoData.getParentId(), iso.getcName(), user.getCompanyId());
                if (StringUtils.isNotNull(isoUnique) && !isoUnique.getIsoId().equals(iso.getIsoId())) {
                    throw new BusinessException("文件名称重复，请重新输入");
                }
            }
            iso.setcTime(new Date()); // 更新修改时间
            return isoMapper.updateIso(iso);
        }
        return 0;
    }

    /**
     * 删除文件管理对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteIsoByIds(String ids) {
        return isoMapper.deleteIsoByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除文件管理
     *
     * @param isoId 文件管理id
     * @return 结果
     */
    @Override
    public int deleteIsoById(Integer isoId) {
        User user = JwtUtil.getTokenUser(ServletUtils.getRequest());
        if (user == null || user.getCompanyId() == null) return 0;
        DevCompany company = devCompanyMapper.selectDevCompanyById(user.getCompanyId());
        if (company == null) return 0;
        Iso iso = isoMapper.selectIsoById(isoId);
        if ((company.getFileSize() - iso.getfSize()) > 0) {
            company.setFileSize(company.getFileSize() - iso.getfSize());
        } else {
            company.setFileSize(0);
        }
        devCompanyMapper.updateDevCompany(company);
        if (StringUtils.isNotNull(iso) && StringUtils.isNotEmpty(iso.getDisk())) {
            // 判断是文件还是文件夹
            if (FileConstants.ITYPE_FOLDER.equals(iso.getiType())) { // 删除文件夹
                //删除对应的文件
                File file = new File(iso.getDisk());
                file.delete();
            } else { // 删除文件
                File file = new File(iso.getDisk() + File.separator + iso.getcName());
                file.delete();
            }
        }

        return isoMapper.deleteIsoById(isoId);
    }

    /**
     * 通过父文件id查询子文件列表
     *
     * @param parentId 父菜单id
     * @return 结果
     */
    @Override
    public List<Iso> selectIsoByParentId(Integer parentId) {
        return isoMapper.selectByPid(parentId);
    }

    /**
     * 根据父id和产线id查询对应产线所以未配置的SOP指导书
     *
     * @param pid    父id
     * @param lineId 产线id
     * @param sopTag sop配置标记状态流水线或者车间
     * @return
     */
    @Override
    public List<Iso> selectNotConfigByPidAndLineId(Integer pid, Integer lineId, Integer sopTag) {
        return isoMapper.selectNotConfigByPidAndLineId(pid, lineId, sopTag);
    }

    /**
     * 上传sop文件
     *
     * @param file     文件
     * @param parentId 父id
     * @param request  请求
     * @return 结果
     */
    @Override
    public int uploadSop(MultipartFile file, int parentId, HttpServletRequest request) throws IOException {
        User user = JwtUtil.getTokenUser(request);
        if (user == null) {
            throw new BusinessException(UserConstants.NOT_LOGIN);
        }
        //计数文件大小
        String fileSize = "0B";
        Long size = file.getSize();
        //查询对应公司是否存在
        DevCompany devCompany = devCompanyMapper.selectDevCompanyById(user.getCompanyId());
        if (devCompany == null) throw new IOException("公司不存在");
        if (devCompany.getSign() == 1) {//VIP用户可用20G
            long fileTotalSize = devCompany.getFileSize() + size;
            long totalSize = 21474386480L;
            if (totalSize < fileTotalSize) {
                throw new IOException("文件存储空间容量不足");
            }
        } else {
            //普通用户 2G
            long fileTotalSize = devCompany.getFileSize() + size;
            long totalSize = 2147438648L;
            if (totalSize < fileTotalSize) {
                throw new IOException("文件存储空间容量不足,请升级");
            }
        }
        devCompany.setFileSize(devCompany.getFileSize() + size);
        devCompanyMapper.updateDevCompany(devCompany);
        long b = 1024L;
        long kb = 1048576L;
        long mb = 1073741824L;
        if (size < b) {//B
            fileSize = size + "B";
        } else if (b <= size && size < kb) {//KB
            fileSize = String.format("%.2f", (size.doubleValue()) / b) + "KB";
        } else if (kb <= size && size < mb) {
            fileSize = String.format("%.2f", (size.doubleValue()) / kb) + "MB";
        } else {
            fileSize = String.format("%.2f", (size.doubleValue()) / mb) + "G";
        }

        // 子文件
        Iso iso = new Iso();
        iso.setCompanyId(user.getCompanyId());
        iso.setcId(user.getUserId().intValue());
        iso.setcTime(new Date());
        iso.setiType(FileConstants.ITYPE_FILE); // 类型为文件

        iso.setFileSize(fileSize);
        // 查询父文件夹信息
        Iso parentIso = isoMapper.selectIsoById(parentId);
        if (StringUtils.isNotNull(parentIso)) {
            String fileName = file.getOriginalFilename(); // 文件名
            // 判断相同文件夹下文件名是否存在相同文件
            Iso isoUnique = isoMapper.selectIsoByUploadName(parentIso.getDiskPath(), fileName);
            if (StringUtils.isNotNull(isoUnique)) { // 存在相同文件名的文件
                throw new BusinessException("文件" + fileName + "已存在");
            }
            if (FileConstants.CATEGORY_SOP_FOLDER.equals(parentIso.getCategory())) { // 为SOP作业指导书下的文件
                iso.setCategory(FileConstants.CATEGORY_SOP_FILE); // 分类为sop下的文件
            }
            if (StringUtils.isNotEmpty(parentIso.getDisk())) {
                String path = parentIso.getDisk() + "/";
                File desc = FileUploadUtils.getAbsoluteFile(path, path + fileName);
                file.transferTo(desc);
            }
            iso.setDiskPath(parentIso.getDiskPath());
            iso.setDisk(parentIso.getDisk());
            iso.setParentId(parentIso.getIsoId());
            iso.setGrParentId(parentIso.getParentId());
            iso.seteName(fileName.substring(0, fileName.lastIndexOf("."))); // 设置文件名
            iso.setcName(fileName); // 设置文件名包括后缀
            iso.setPath((isoFileUrl + parentIso.getDiskPath() + "/" + fileName));
            iso.setIsoId(null);
            iso.setfSize(size);
            return isoMapper.insertIso(iso);
        }
        return 0;
    }


    /**
     * 根据硬件编码查询对应的作业指导书
     *
     * @param apiActiveCode 硬件编码
     * @return 结果
     */
    @Override
    public Map<String, Object> selectSopByDevCode(ApiActiveCode apiActiveCode) {
        Map<String, Object> map = new HashMap<>(16);
        // 查询激活码是否存在
        if (apiActiveCode == null || StringUtils.isEmpty(apiActiveCode.getCode()) || StringUtils.isEmpty(apiActiveCode.getWatchCode())) {
            map.put("code", 0);
            map.put("msg", "硬件或激活码不能为空");
            return map;
        }
        String toUpperWatchCode = apiActiveCode.getWatchCode().toUpperCase();
        ActiveCode uniqueActiveCode = activeCodeMapper.selctActiveCodeByCode(apiActiveCode.getCode());
        if (uniqueActiveCode == null) {
            map.put("code", 2);
            map.put("msg", "无效激活码");
            return map;
        }

        DevList devList = devListMapper.selectDevListByCode(toUpperWatchCode);
        if (devList == null || devList.getCompanyId() == null) {
            map.put("code", 0);
            map.put("msg", "硬件不存在或未归属公司");
            return map;
        }
        // 查询扫码硬件有没有绑定过
        ActiveCode activeCodeByImei = activeCodeMapper.selectActiveCodeByImei(toUpperWatchCode);
        if (StringUtils.isNotNull(activeCodeByImei) && !apiActiveCode.getCode().equals(activeCodeByImei.getCode())) {
            map.put("code", 0);
            map.put("msg", "该硬件已被绑定");
            return map;
        }
        // 更新硬件绑定信息
        uniqueActiveCode.setImei(toUpperWatchCode);
        uniqueActiveCode.setActSign(1);
        uniqueActiveCode.setCompanyId(devList.getCompanyId());
        activeCodeMapper.updateActiveCode(uniqueActiveCode);

        if (devList.getDeviceStatus().equals(DevConstants.DEV_STATUS_NO)) {
            map.put("code", 0);
            map.put("msg", "硬件被禁用");
            return map;
        }
        if (devList.getDevType() == null || devList.getSign().equals(DevConstants.DEV_SIGN_NOT_USE)) {
            map.put("code", 0);
            map.put("msg", "硬件未被配置");
            return map;
        }
        //查询对应公司信息
        DevCompany company = companyMapper.selectDevCompanyById(devList.getCompanyId());
        if (company == null) {
            map.put("code", 0);
            map.put("msg", "公司信息不存在");
            return map;
        }
        // 设置作业指导书看板相关信息
        SopApi sopApi = new SopApi();
        //根据硬件编码查询对应的工位信息
        Workstation workstation = workstationMapper.selectByDevCode(devList.getCompanyId(),toUpperWatchCode);
        if (workstation == null) {
            map.put("code", 0);
            map.put("msg", "工位不存在");
            return map;
        }
        //查询对应产线
        ProductionLine line = productionLineMapper.selectProductionLineById(workstation.getLineId());
        if (line == null) {
            map.put("code", 0);
            map.put("msg", "产线不存在");
            return map;
        }
        sopApi.setlName(line.getLineName());
        sopApi.setwName(workstation.getwName());
        sopApi.setCompany(company.getComName());
        //查询正在进行的工单
        DevWorkOrder workOrder = devWorkOrderMapper.selectWorkByCompandAndLine(line.getCompanyId(), workstation.getLineId(), WorkConstants.SING_LINE);
        if (workOrder == null || StringUtils.isEmpty(workOrder.getProductCode())) {
            map.put("code", 0);
            map.put("msg", "没有正在进行的工单");
            map.put("data", sopApi);
            return map;
        }

        //修改对应工位ASOP反馈标识
        workstationMapper.updateAllResSignById(workstation.getId(), 1);
        int num = workstationMapper.countAllResSignByCompanyIdAndLineId(workOrder.getCompanyId(), workOrder.getLineId());
        if (num <= 0) {
            devWorkOrderMapper.updateWorkOrderResSign(workOrder.getId(), 1);
        }
        //查询对应SOP配置
        SopLine sopLine = sopLineMapper.selectSopByCidAndLineIdAndPidAndWid(workstation.getCompanyId(), line.getId(), workOrder.getProductCode(), workstation.getId());
        if (sopLine == null) {
            map.put("code", 0);
            map.put("msg", "没有配置SOP");
            map.put("data", sopApi);
            return map;
        }
        //查询对应SOP
        Iso iso = isoMapper.selectIsoById(sopLine.getPageId());
        if (iso == null) {
            map.put("code", 0);
            map.put("msg", "没有配置SOP");
            map.put("data", sopApi);
            return map;
        }
        iso.setFileSize(line.getLineName() + " " + workstation.getwName());
        iso.setcId(0);
        try {
            sopApi = getSopApi(sopApi,map, workOrder, iso);
        } catch (Exception e) {
            map.put("code", 0);
            map.put("msg", "请求失败");
            return map;
        }
        map.put("data", sopApi);
        map.put("code", 1);
        map.put("msg", "请求成功");
        return map;

    }

    /**
     * 设置作业指导书看板相关信息
     *
     * @param map       参数项
     * @param workOrder 工单
     * @param iso       sop
     * @return 结果
     */
    private SopApi getSopApi(SopApi sopApi,Map<String, Object> map, DevWorkOrder workOrder, Iso iso) throws UnsupportedEncodingException {
        int index = iso.getPath().lastIndexOf("/");
        String path = iso.getPath().substring(0, index);
        String fileName = iso.getPath().substring(index + 1);
        map.put("iso", iso);
        sopApi.setwCode(workOrder.getWorkorderNumber());
        sopApi.setwStatus(workOrder.getOperationStatus());
        sopApi.setpCode(workOrder.getProductCode());
        sopApi.setpName(workOrder.getProductName());
        sopApi.setIsoId(iso.getIsoId());
        sopApi.setIsoPath(iso.getPath());
        sopApi.setPath(path + "/" + URLEncoder.encode(fileName, "UTF-8"));
        return sopApi;
    }

    @Override
    public List<Iso> selectNotConfigBySwId(int parentId, int lineId) {
        return null;
    }

    /**
     * app端查询ISO文件系统
     *
     * @param iso 文件体系对象
     * @return 结果
     */
    @Override
    public List<Iso> appSelectList(Iso iso) {
        User user = JwtUtil.getUser();
        if (user == null) {
            return Collections.emptyList();
        }
        iso.setCompanyId(user.getCompanyId());
        return isoMapper.selectIsoList(iso);
    }

    /**
     * 根据公司id和父id查询作业指导书
     *
     * @return
     */
    @Override
    public List<Iso> selectAllIsoByPidAndCompanyId() {
        User user = JwtUtil.getUser();
        return isoMapper.selectAllIsoByPidAndCompanyId(user.getCompanyId(), 0);
    }
}
