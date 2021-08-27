
package com.mockitotutorial.happyhotel.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Test12Bdd {

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
    void should_CountAvailablePlaces_When_OneRoomAvailable() {
        // Given
        given(this.roomServiceMock.getAvailableRooms()).willReturn(Collections.singletonList(new Room("Room 1", 2)));
        int expected = 2;

        // When
        int actual = bookingService.getAvailablePlaceCount();

        // Then
        assertEquals(expected, actual);
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
        then(paymentServiceMock).should(times(1)).pay(bookingRequest, 400.00);
        verifyNoMoreInteractions(paymentServiceMock);
    }
}
