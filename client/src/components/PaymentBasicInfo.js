import React from 'react';
import moment from 'moment';

const PaymentBasicInfo = ({ payment }) => {
    return <>
        <p>
            <b>Payment Id:</b> {payment.id}
        </p>

        <p>
            <b>Amount of money:</b> {payment.amountOfMoney} PLN
        </p>

        <p>
            <b>Payment method:</b> {payment.methodCode}
        </p>

        <p>
            <b>Paid in:</b> {moment(payment.dateCreated).format('YYYY-MM-DD hh:mm:ss')}
        </p>
    </>
};

export default PaymentBasicInfo;