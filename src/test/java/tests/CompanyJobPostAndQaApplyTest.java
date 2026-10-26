package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import utils.TestUser;

public class CompanyJobPostAndQaApplyTest extends BaseTest {

    @Test
    public void companyPostAndQaApply() {
        PostedJobPage postedJob = new LoginPage(driver)
                .open()
                .loginAs(TestUser.COMPANY)
                .openJobs()
                .openPostJob()
                .jobTitleFilling()
                .jobDescriptionFilling()
                .jobPostPublishing();

        Assert.assertEquals(postedJob.getTitleText(), "Test text for Job Title information",
"Created job title should match");


        String idJob = postedJob.getJobId();
        postedJob.logOut();


        PostedJobPage jobAfterApply = new LoginPage(driver)
                .open()
                .loginAs(TestUser.QA)
                .openJobs()
                .searchForJobPost()
                .openJobPage()
                .applyProcess();

        Assert.assertEquals(jobAfterApply.getApplicationMessageText(), "Application sent!",
                "Application approved message should be sent");

    }
}
