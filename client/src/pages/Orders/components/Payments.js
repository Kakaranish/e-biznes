import React from 'react';
import Payment from './Payment';

const Payments = (props) => {

    const payments = props.payments;

    return <div className="mb-5">
        <h3>Payments</h3>
        {!payments || payments.length === 0
            ? <p>You have no payments yet</p>
            : payments.map(payment => <Payment payment={payment} key={`p-${payment.id}`} />)
        }
    </div>
};

export default Payments;