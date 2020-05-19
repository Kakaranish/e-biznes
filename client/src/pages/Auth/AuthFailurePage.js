import React from 'react';
import queryString from 'query-string';

const AuthFailurePage = (props) => {

    const queryParams = queryString.parse(props.location.search);
    const errorMessage = getErrorMessage(queryParams.errorCode);

    if (!errorMessage) window.location = '/';
    return <>
        <h3>Auth error</h3>
        <p>{errorMessage}</p>
    </>
};

const getErrorMessage = errorCode => {
    if (errorCode === "XD403") return "Email is bounded to account assigned to other provider";
    else if (errorCode === "XD500") return "Unknown auth error";
    else return null;
}

export default AuthFailurePage;