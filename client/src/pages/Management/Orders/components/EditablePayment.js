import React, { useState } from 'react';
import axios from 'axios';
import AwareComponentBuilder from '../../../../common/AwareComponentBuilder';
import { doRequest, getFormDataJsonFromEvent } from '../../../../common/Utils';
import PaymentBasicInfo from '../../../../components/PaymentBasicInfo';

const EditablePayment = (props) => {
    const payment = props.payment;

    const [inEditMode, setInEditMode] = useState(false);
    const [statusState, setStatusState] = useState(payment.status);
    const [selected, setSelected] = useState(payment.status);

    const onSelect = event => setSelected(event.target.value);
    const onCancel = () => {
        setInEditMode(false);
        setSelected(statusState);
    }

    const onSubmit = async event => {
        event.preventDefault();
        let formData = getFormDataJsonFromEvent(event);
        formData.paymentId = payment.id;

        try {
            const action = async () => axios.put('/api/admin/payments', formData, {
                validateStatus: false,
                headers: { 'X-Auth-Token': props.auth.token }
            });
            await doRequest(action);
            setInEditMode(false);
            setStatusState(selected);
        } catch (error) {
            alert(`${error} error occured`);
        }
    }

    return <div className="p-3 mb-2" style={{ border: "1px solid gray" }} key={`p-${payment.id}`}>
        <PaymentBasicInfo payment={payment} />
        
        {
            !inEditMode
                ? <>
                    <p>
                        <b>Status:&nbsp;

                        {
                                (() => {
                                    switch (statusState) {
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

                    <button className="btn btn-primary w-25" onClick={() => setInEditMode(true)}>
                        Edit status
                    </button>
                </>

                : <>
                    <form onSubmit={onSubmit}>
                        <div className="form-group">
                            <select name="status" value={selected} onChange={onSelect}
                                className="custom-select" required>
                                {
                                    ["PENDING", "ACCEPTED", "REJECTED"].map((status, i) =>
                                        <option key={`opt-${status}`} value={status}>
                                            {status}
                                        </option>
                                    )
                                }
                            </select>
                        </div>

                        <button type="submit" className="btn btn-primary w-25">
                            Update
                        </button>

                        <button className="btn btn-secondary w-25" onClick={onCancel}>
                            Cancel
                        </button>
                    </form>
                </>
        }

    </div>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(EditablePayment);