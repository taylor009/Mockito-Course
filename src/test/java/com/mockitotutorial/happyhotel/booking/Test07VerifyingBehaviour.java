package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class Test07VerifyingBehaviour {

    private BookingService bookingService;
    private PaymentService paymentServiceMock;
    private RoomService roomServiceMock;
    private BookingDAO bookingDAOMock;
    private MailSender mailSenderMock;

    @BeforeEach
    void setUp() {

        this.paymentServiceMock = mock(PaymentService.class);
        this.roomServiceMock = mock(RoomService.class);
        this.bookingDAOMock = mock(BookingDAO.class);
        this.mailSenderMock = mock(MailSender.class);


        this.bookingService = new BookingService(paymentServiceMock, roomServiceMock, bookingDAOMock, mailSenderMock);
    }

    @Test
    void should_InvokePayment_When_Prepaid() {
        // Given
        BookingRequest bookingRequest = new BookingRequest("1",
                LocalDate.of(2020, 01, 01),
                LocalDate.of(2020, 01, 05), 2, true);

        // When
        bookingService.makeBooking(bookingRequest);

        // Then
        verify(paymentServiceMock, times(1)).pay(bookingRequest, 400.00);
        verifyNoMoreInteractions(paymentServiceMock);

    }

    @Test
    void should_NotInvokePayment_When_NotPrepaid() {
        // Given
        BookingRequest bookingRequest = new BookingRequest("1",
                LocalDate.of(2020, 01, 01),
                LocalDate.of(2020, 01, 05), 2, false);

        // When
        bookingService.makeBooking(bookingRequest);

        // Then
        verify(paymentServiceMock, never()).pay(any(), anyDouble());
    }
}
