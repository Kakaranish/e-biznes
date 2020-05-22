import React from 'react';
import { connect } from 'react-redux';
import { logOut } from '../pages/Auth/duck/actions';

const Logout = (props) => {

    const onClick = () => {
        if (!props.auth) {
            alert('Cannot logout because no one is logged in.');
            return;
        }
        
        localStorage.removeItem('auth');
        props.logOut();
    };

    return <div onClick={onClick}>
        Log out
    </div>
};

const mapStateToProps = state => ({
    auth: state.auth
});

const mapDispatchToProps = dispatch => ({
    logOut: () => dispatch(logOut())
});

export default connect(mapStateToProps, mapDispatchToProps)(Logout);