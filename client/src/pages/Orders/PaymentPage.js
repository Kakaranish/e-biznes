import React from 'react';
import { createAuthAwareComponent } from '../Auth/Utils';

const PaymentPage = (props) => {
    return <>
        Payment Page
    </>
};

export default createAuthAwareComponent(PaymentPage);