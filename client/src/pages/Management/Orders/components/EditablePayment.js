import React, { useState } from 'react';
import axios from 'axios';
import moment from 'moment';
import AwareComponentBuilder from '../../../../common/AwareComponentBuilder';
import { doRequest, getFormDataJsonFromEvent } from '../../../../Utils';

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

        {
            !inEditMode
                ? <>
                    <p>
                        <b>Status:</b> {statusState}
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