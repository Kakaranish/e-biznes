import React, { useState } from 'react';
import AwareComponentBuilder from '../../../common/AwareComponentBuilder';
import { doRequest } from '../../../common/Utils';
import axios from 'axios';
import PaymentBasicInfo from '../../../components/PaymentBasicInfo';

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
        <PaymentBasicInfo payment={payment} />

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