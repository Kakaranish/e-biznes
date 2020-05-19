import React from 'react';
import { useHistory } from 'react-router-dom';
import queryString from 'query-string';

const AuthSuccessfulPage = (props) => {
    
    const history = useHistory();

    const queryParams = queryString.parse(props.location.search);
    localStorage.setItem('token', queryParams.token);
    localStorage.setItem('email', queryParams.email);
    localStorage.setItem('expiryDatetime', queryParams.expiryDatetime);

    history.push('/');

    return <></>
};

export default AuthSuccessfulPage;