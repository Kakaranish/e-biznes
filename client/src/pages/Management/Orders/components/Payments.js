import React from 'react';
import EditablePayment from './EditablePayment';

const Payments = ({ payments }) => {
    return <div className="mb-5">
        <h3>Payments</h3>
        {!payments || payments.length === 0
            ? <p>No payments yet</p>

            : payments.map((p, i) =>
                <EditablePayment payment={p} key={`p-${p.id}`} />
            )
        }
    </div>
};

export default Payments;