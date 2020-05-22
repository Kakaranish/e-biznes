import React from 'react';
import { connect } from 'react-redux';
import { useHistory } from 'react-router-dom';
import queryString from 'query-string';
import { logIn } from './duck/actions';

const AuthSuccessfulPage = (props) => {

    const history = useHistory();
    
    const queryParams = queryString.parse(props.location.search);
    const auth = {
        token: queryParams.token,
        tokenExpiry: parseInt(queryParams.tokenExpiry),
        email: queryParams.email,
        role: queryParams.role
    };
    props.logIn(auth);
    history.push('/');

    return <></>
};

const mapDispatchToProps = dispatch => ({
    logIn: item => dispatch(logIn(item))
});

export default connect(null, mapDispatchToProps)(AuthSuccessfulPage);