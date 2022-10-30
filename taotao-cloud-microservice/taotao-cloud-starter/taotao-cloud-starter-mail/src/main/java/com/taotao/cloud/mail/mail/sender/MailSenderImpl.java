package com.taotao.cloud.mail.mail.sender;

import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.mail.mail.event.MailSendEvent;
import com.taotao.cloud.mail.mail.model.MailDetails;
import com.taotao.cloud.mail.mail.model.MailSendInfo;
import java.io.File;
import java.time.LocalDateTime;
import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/2/27 17:06
 */
public class MailSenderImpl implements MailSender {

	private final JavaMailSender mailSender;

	private final ApplicationEventPublisher eventPublisher;

	/**
	 * 配置文件中我的qq邮箱
	 */
	@Value("${spring.mail.properties.from}")
	private String defaultFrom;

	public MailSenderImpl(JavaMailSender mailSender, ApplicationEventPublisher eventPublisher) {
		this.mailSender = mailSender;
		this.eventPublisher = eventPublisher;
	}

	/**
	 * 发送邮件
	 *
	 * @param mailDetails 邮件参数
	 * @return boolean 发送是否成功
	 */
	@Override
	public MailSendInfo sendMail(MailDetails mailDetails) {
		MailSendInfo mailSendInfo = new MailSendInfo(mailDetails);
		mailSendInfo.setSentDate(LocalDateTime.now());

		try {
			// 1.检测邮件
			checkMail(mailDetails);
			// 2.发送邮件
			sendMimeMail(mailDetails);
			mailSendInfo.setSuccess(true);
		} catch (Exception e) {
			mailSendInfo.setSuccess(false);
			mailSendInfo.setErrorMsg(e.getMessage());
			LogUtils.error("发送邮件失败: [{}]", mailDetails, e);
		} finally {
			// 发布邮件发送事件
			eventPublisher.publishEvent(new MailSendEvent(mailSendInfo));
		}
		return mailSendInfo;
	}

	/**
	 * 构建复杂邮件信息类
	 *
	 * @param mailDetails 邮件发送设置
	 */
	private void sendMimeMail(MailDetails mailDetails) throws MessagingException {
		// true表示支持复杂类型
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(),
			true);
		String from =
			StringUtils.hasText(mailDetails.getFrom()) ? mailDetails.getFrom() : defaultFrom;
		messageHelper.setFrom(from);
		messageHelper.setSubject(mailDetails.getSubject());
		if (mailDetails.getTo() != null && mailDetails.getTo().length > 0) {
			messageHelper.setTo(mailDetails.getTo());
		}
		if (mailDetails.getCc() != null && mailDetails.getCc().length > 0) {
			messageHelper.setCc(mailDetails.getCc());
		}
		if (mailDetails.getBcc() != null && mailDetails.getBcc().length > 0) {
			messageHelper.setBcc(mailDetails.getBcc());
		}
		// 是否展示html
		boolean showHtml = mailDetails.getShowHtml() != null && mailDetails.getShowHtml();
		messageHelper.setText(mailDetails.getContent(), showHtml);
		if (mailDetails.getFiles() != null) {
			for (File file : mailDetails.getFiles()) {
				messageHelper.addAttachment(file.getName(), file);
			}
		}

		mailSender.send(messageHelper.getMimeMessage());
		LogUtils.info("发送邮件成功：[{}]", mailDetails);
	}

}
