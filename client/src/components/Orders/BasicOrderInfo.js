import React from 'react';
import moment from 'moment';

const BasicOrderInfo = ({ orderState, showUser }) => {
    return <>
        <h3>Order {orderState.orderInfo.order.id}</h3>

        {
            showUser &&
            <p>
                User: {orderState.orderInfo.order.userId}
            </p>
        }

        <p>Created: {moment(orderState.orderInfo.order.dateCreated).format('YYYY-MM-DD hh:mm:ss')}</p>

        <p>
            <b>Paid? </b>
            {
                orderState.toPay <= 0
                    ? <span className="text-success">Yes</span>
                    : <span className="text-danger">No</span>
            }
        </p>

        <p>
            <b>Total Price:</b> {orderState.totalPrice.toFixed(2)} PLN
        </p>

        <p>
            <b>Money paid in: </b> {orderState.paymentsValue.toFixed(2)} PLN
        </p>

        <p>
            <b>Remaining money to paid:</b> {orderState.toPay} PLN
        </p>
    </>
};

export default BasicOrderInfo;