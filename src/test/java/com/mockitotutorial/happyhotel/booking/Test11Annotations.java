package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Test11Annotations {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private PaymentService paymentServiceMock;
    @Mock
    private RoomService roomServiceMock;
    @Mock
    private BookingDAO bookingDAOMock;
    @Mock
    private MailSender mailSenderMock;
    @Captor
    private ArgumentCaptor<Double> doubleCaptor;

    @Test
    void should_PayCorrectPrice_When_InputOK() {
        // Given
        BookingRequest bookingRequest = new BookingRequest("1",
                LocalDate.of(2020, 01, 01),
                LocalDate.of(2020, 01, 05), 2, true);

        // When
        bookingService.makeBooking(bookingRequest);

        // Then
        verify(paymentServiceMock, times(1)).pay(eq(bookingRequest), doubleCaptor.capture());
        double capturedArgument = doubleCaptor.getValue();

        assertEquals(400.0, capturedArgument);
    }

    @Test
    void should_PayCorrectPrice_When_MultipleCalls() {
        // Given
        BookingRequest bookingRequest = new BookingRequest("1",
                LocalDate.of(2020, 01, 01),
                LocalDate.of(2020, 01, 05), 2, true);
        BookingRequest bookingRequest2 = new BookingRequest("1",
                LocalDate.of(2020, 01, 01),
                LocalDate.of(2020, 01, 02), 2, true);
        List<Double> expectedValues = Arrays.asList(400.0, 100.0);

        // When
        bookingService.makeBooking(bookingRequest);
        bookingService.makeBooking(bookingRequest2);

        // Then
        verify(paymentServiceMock, times(2)).pay(any(), doubleCaptor.capture());
        List<Double> capturedArguments = doubleCaptor.getAllValues();

        assertEquals(expectedValues, capturedArguments);
    }
}
