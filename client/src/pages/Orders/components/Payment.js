import React, { useState } from 'react';
import moment from 'moment';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';
import { doRequest } from '../../../Utils';
import axios from 'axios';

const Payment = (props) => {

    const payment = props.payment;
    const [status, setStatus] = useState(payment.status);

    const onCancel = async () => {
        try {
            const action = async () => axios.put('/api/payments/cancel',
                { paymentId: payment.id },
                {
                    validateStatus: false,
                    headers: { 'X-Auth-Token': props.auth.token }
                });
            await doRequest(action);
            setStatus("CANCELLED");
        } catch (error) {
            alert(`${error} error occured`);
        }
    };

    return <div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`p-${payment.id}`}>

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

        <p>
            <b>Status:&nbsp;
        {
                    (() => {
                        switch (status) {
                            case "CANCELLED":
                                return <span style={{ color: "gray" }}>Cancelled</span>
                            case "PENDING":
                                return <span style={{ color: "orange" }}>Pending</span>
                            case "ACCEPTED":
                                return <span style={{ color: "green" }}>Accepted</span>
                            case "REJECTED":
                                return <span style={{ color: "red" }}>Rejected</span>
                        }
                    })()
                }
            </b>
        </p>

        {
            status === "PENDING" &&
            <button className="btn btn-secondary" onClick={onCancel}>
                Cancel payment
            </button>
        }
    </div>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(Payment);