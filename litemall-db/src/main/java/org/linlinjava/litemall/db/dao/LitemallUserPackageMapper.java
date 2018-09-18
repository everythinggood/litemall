package org.linlinjava.litemall.db.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallUserPackage;
import org.linlinjava.litemall.db.domain.LitemallUserPackageExample;

public interface LitemallUserPackageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     */
    long countByExample(LitemallUserPackageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int deleteWithVersionByExample(@Param("version") Integer version, @Param("example") LitemallUserPackageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     */
    int deleteByExample(LitemallUserPackageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int deleteWithVersionByPrimaryKey(@Param("version") Integer version, @Param("key") Integer key);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     */
    int insert(LitemallUserPackage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     */
    int insertSelective(LitemallUserPackage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    LitemallUserPackage selectOneByExample(LitemallUserPackageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    LitemallUserPackage selectOneByExampleSelective(@Param("example") LitemallUserPackageExample example, @Param("selective") LitemallUserPackage.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    List<LitemallUserPackage> selectByExampleSelective(@Param("example") LitemallUserPackageExample example, @Param("selective") LitemallUserPackage.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     */
    List<LitemallUserPackage> selectByExample(LitemallUserPackageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    LitemallUserPackage selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") LitemallUserPackage.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     */
    LitemallUserPackage selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    LitemallUserPackage selectByPrimaryKeyWithLogicalDelete(@Param("id") Integer id, @Param("andLogicalDeleted") boolean andLogicalDeleted);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int updateWithVersionByExample(@Param("version") Integer version, @Param("record") LitemallUserPackage record, @Param("example") LitemallUserPackageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int updateWithVersionByExampleSelective(@Param("version") Integer version, @Param("record") LitemallUserPackage record, @Param("example") LitemallUserPackageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") LitemallUserPackage record, @Param("example") LitemallUserPackageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") LitemallUserPackage record, @Param("example") LitemallUserPackageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int updateWithVersionByPrimaryKey(@Param("version") Integer version, @Param("record") LitemallUserPackage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int updateWithVersionByPrimaryKeySelective(@Param("version") Integer version, @Param("record") LitemallUserPackage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(LitemallUserPackage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(LitemallUserPackage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int logicalDeleteByExample(@Param("example") LitemallUserPackageExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_user_package
     *
     * @mbg.generated
     * @project https://github.com/itfsw/mybatis-generator-plugin
     */
    int logicalDeleteByPrimaryKey(Integer id);
}