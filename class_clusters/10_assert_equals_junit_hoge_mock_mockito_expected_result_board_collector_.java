package com.example.guava;

import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.example.db.Member;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Ordering;

/**
 * @author gimbyeongsu
 *
 */
public class OrderingTest extends CommonMember {
	private final static Logger LOGGER = Logger.getLogger(OrderingTest.class);
	
	@Test
	public void test000() {
		LOGGER.debug("Ordering implements Comparable");
		
		LOGGER.debug("List 객채의 Comparable구현 내용 compareTo 조건으로 정렬");
		
		List<Member> members = dao.listAll();
		List<Member> membersSort = Ordering.natural().sortedCopy(members);
		
		for (Member member : membersSort) {
			LOGGER.debug(member.toString());
		}
	}
	
	@Test
	public void test001() {
		LOGGER.debug("Ordering from");
		
		LOGGER.debug("List 객채의 Comparable를 사용하지 않고 정렬");
		
		List<Member> members = dao.listAll();
		List<Member> membersSort = Ordering.from(new Comparator<Member>() {

			@Override
			public int compare(Member o1, Member o2) {
				return ComparisonChain.start().compare(o1.age, o2.age).result();
			}
		}).sortedCopy(members);

		for (Member member : membersSort) {
			LOGGER.debug(member.toString());
		}
	}
	
	@Test
	public void test002() {
		LOGGER.debug("Ordering max min");
		
		LOGGER.debug("해당 List에서 조건에 해당하는 걸로 정렬후 max");
		List<Member> members = dao.listAll();
		Member memberMax = Ordering.from(new Comparator<Member>() {

			@Override
			public int compare(Member o1, Member o2) {
				return ComparisonChain.start().compare(o1.age, o2.age).compare(o1.id, o2.id).result();
			}
		}).max(members);
		LOGGER.debug("max:" + memberMax);
		
		LOGGER.debug("해당 List에서 조건에 해당하는 걸로 정렬후 min");
		Member memberMin = Ordering.from(new Comparator<Member>() {

			@Override
			public int compare(Member o1, Member o2) {
				return ComparisonChain.start().compare(o1.age, o2.age).compare(o1.id, o2.id).result();
			}
		}).min(members);
		LOGGER.debug("min:" + memberMin);
	}
}

--------------------

package com.maciejwalkowiak.mercury.core.message;

import com.maciejwalkowiak.mercury.mail.common.SendMailRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MessengerTest {
	@InjectMocks
	private Messenger messenger;
	@Mock
	private MessageService messageService;
	@Mock
	private MessageNotifier messageNotifier;

	private final SendMailRequest request = new SendMailRequest.Builder()
			.to("foo@bar.com")
			.subject("subject")
			.text("content")
			.build();

	@Test
	public void shouldSaveMessage() {
		messenger.publish(request);

		ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);

		verify(messageService).save(captor.capture());
		assertThat(captor.getValue().getRequest()).isEqualTo(request);
		assertThat(captor.getValue().getStatus()).isEqualTo(Message.Status.QUEUED);
	}

	@Test
	public void shouldPublishMessage() {
		// when
		messenger.publish(request);

		// then
		ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);

		verify(messageNotifier).notifyConsumers(captor.capture());

		Message message = captor.getValue();

		assertThat(message.getRequest()).isEqualTo(request);
	}
}
--------------------

package org.ei.drishti.repository;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.ei.drishti.domain.ReportIndicator.CONDOM;
import static org.ei.drishti.domain.ReportIndicator.EARLY_ANC_REGISTRATIONS;
import static org.ei.drishti.domain.ReportIndicator.IUD;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.util.List;

import org.ei.drishti.domain.Report;
import org.ei.drishti.dto.Action;
import org.ei.drishti.util.ActionBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class AllReportsTest {
    @Mock
    private ReportRepository repository;
    private AllReports allReports;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        allReports = new AllReports(repository);
    }

    @Test
    public void shouldDelegateActionToReportRepository() throws Exception {
        Action iudAction = ActionBuilder.actionForReport("IUD", "40");

        allReports.handleAction(iudAction);

        verify(repository).update(new Report("IUD", "40", "some-month-summary-json"));
    }

    @Test
    public void shouldGetReportsForGivenIndicators() throws Exception {
        List<Report> expectedReports = asList(new Report("IUD", "40", "some-month-summary-json"), new Report("ANC_LT_12", "30", "some-month-summary-json"));
        when(repository.allFor("IUD", "CONDOM", "ANC_LT_12")).thenReturn(expectedReports);

        List<Report> reports = allReports.allFor(asList(IUD, CONDOM, EARLY_ANC_REGISTRATIONS));

        assertEquals(expectedReports, reports);
    }
}

--------------------

