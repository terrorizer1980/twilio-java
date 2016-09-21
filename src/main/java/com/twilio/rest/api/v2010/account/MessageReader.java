/**
 * This code was generated by
 * \ / _    _  _|   _  _
 *  | (_)\/(_)(_|\/| |(/_  v1.0.0
 *       /       /       
 */

package com.twilio.rest.api.v2010.account;

import com.google.common.collect.Range;
import com.twilio.base.Page;
import com.twilio.base.Reader;
import com.twilio.base.ResourceSet;
import com.twilio.converter.DateConverter;
import com.twilio.exception.ApiConnectionException;
import com.twilio.exception.ApiException;
import com.twilio.exception.RestException;
import com.twilio.http.HttpMethod;
import com.twilio.http.Request;
import com.twilio.http.Response;
import com.twilio.http.TwilioRestClient;
import com.twilio.rest.Domains;
import org.joda.time.DateTime;

public class MessageReader extends Reader<Message> {
    private String accountSid;
    private com.twilio.type.PhoneNumber to;
    private com.twilio.type.PhoneNumber from;
    private DateTime absoluteDateSent;
    private Range<DateTime> rangeDateSent;

    /**
     * Construct a new MessageReader.
     */
    public MessageReader() {
    }

    /**
     * Construct a new MessageReader.
     * 
     * @param accountSid The account_sid
     */
    public MessageReader(final String accountSid) {
        this.accountSid = accountSid;
    }

    /**
     * Filter by messages to this number.
     * 
     * @param to Filter by messages to this number
     * @return this
     */
    public MessageReader byTo(final com.twilio.type.PhoneNumber to) {
        this.to = to;
        return this;
    }

    /**
     * Only show messages from this phone number.
     * 
     * @param from Filter by from number
     * @return this
     */
    public MessageReader byFrom(final com.twilio.type.PhoneNumber from) {
        this.from = from;
        return this;
    }

    /**
     * Filter messages sent by this date.
     * 
     * @param absoluteDateSent Filter by date sent
     * @return this
     */
    public MessageReader byDateSent(final DateTime absoluteDateSent) {
        this.rangeDateSent = null;
        this.absoluteDateSent = absoluteDateSent;
        return this;
    }

    /**
     * Filter messages sent by this date.
     * 
     * @param rangeDateSent Filter by date sent
     * @return this
     */
    public MessageReader byDateSent(final Range<DateTime> rangeDateSent) {
        this.absoluteDateSent = null;
        this.rangeDateSent = rangeDateSent;
        return this;
    }

    /**
     * Make the request to the Twilio API to perform the read.
     * 
     * @param client TwilioRestClient with which to make the request
     * @return Message ResourceSet
     */
    @Override
    public ResourceSet<Message> execute(final TwilioRestClient client) {
        return new ResourceSet<>(this, client, firstPage());
    }

    /**
     * Make the request to the Twilio API to perform the read.
     * 
     * @param client TwilioRestClient with which to make the request
     * @return Message ResourceSet
     */
    @Override
    @SuppressWarnings("checkstyle:linelength")
    public Page<Message> firstPage(final TwilioRestClient client) {
        this.accountSid = this.accountSid == null ? client.getAccountSid() : this.accountSid;
        Request request = new Request(
            HttpMethod.GET,
            Domains.API.toString(),
            "/2010-04-01/Accounts/" + this.accountSid + "/Messages.json",
            client.getRegion()
        );
        
        addQueryParams(request);
        return pageForRequest(client, request);
    }

    /**
     * Retrieve the next page from the Twilio API.
     * 
     * @param page current page
     * @param client TwilioRestClient with which to make the request
     * @return Next Page
     */
    @Override
    public Page<Message> nextPage(final Page<Message> page, 
                                  final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            page.getNextPageUrl(
                Domains.API.toString(),
                client.getRegion()
            )
        );
        return pageForRequest(client, request);
    }

    /**
     * Generate a Page of Message Resources for a given request.
     * 
     * @param client TwilioRestClient with which to make the request
     * @param request Request to generate a page for
     * @return Page for the Request
     */
    private Page<Message> pageForRequest(final TwilioRestClient client, final Request request) {
        Response response = client.request(request);
        
        if (response == null) {
            throw new ApiConnectionException("Message read failed: Unable to connect to server");
        } else if (!TwilioRestClient.SUCCESS.apply(response.getStatusCode())) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            if (restException == null) {
                throw new ApiException("Server Error, no content");
            }
        
            throw new ApiException(
                restException.getMessage(),
                restException.getCode(),
                restException.getMoreInfo(),
                restException.getStatus(),
                null
            );
        }
        
        return Page.fromJson(
            "messages",
            response.getContent(),
            Message.class,
            client.getObjectMapper()
        );
    }

    /**
     * Add the requested query string arguments to the Request.
     * 
     * @param request Request to add query string arguments to
     */
    private void addQueryParams(final Request request) {
        if (to != null) {
            request.addQueryParam("To", to.toString());
        }
        
        if (from != null) {
            request.addQueryParam("From", from.toString());
        }
        
        if (absoluteDateSent != null) {
            request.addQueryParam("DateSent", absoluteDateSent.toString(Request.QUERY_STRING_DATE_TIME_FORMAT));
        } else if (rangeDateSent != null) {
            request.addQueryDateTimeRange("DateSent", rangeDateSent);
        }
        
        if (getPageSize() != null) {
            request.addQueryParam("PageSize", Integer.toString(getPageSize()));
        }
    }
}