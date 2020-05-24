import { connect } from 'react-redux';
import { logIn, logOut } from './pages/Auth/duck/actions';
import { setNotifs, clearNotifs } from './pages/Notifications/ducks/actions';
import axios from 'axios';

export const isValidUUID = value =>
    /^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$/i.test(value);

export const getFormDataJsonFromEvent = event => {
    const formData = new FormData(event.target);

    let formDataJson = {};
    for (const [key, value] of formData.entries()) {
        formDataJson[key] = value;
    }

    return formDataJson;
}

export const createAuthAwareComponent = funcComponent => {
    const mapStateToProps = state => ({
        auth: state.auth
    });

    const mapDispatchToProps = dispatch => ({
        logIn: item => dispatch(logIn(item)),
        logOut: () => dispatch(logOut())
    });

    return connect(mapStateToProps, mapDispatchToProps)(funcComponent);
};

export const createAuthAndNotifAwareComponent = funcComponent => {
    const mapStateToProps = state => ({
        auth: state.auth,
        notifs: state.notifs
    });

    const mapDispatchToProps = dispatch => ({
        logIn: item => dispatch(logIn(item)),
        logOut: () => dispatch(logOut()),
        setNotifs: notifs => dispatch(setNotifs(notifs)),
        clearNotifs: () => dispatch(clearNotifs())
    });

    return connect(mapStateToProps, mapDispatchToProps)(funcComponent);
};

export const fetchNotifications = async authInfo => {
    if(!authInfo || authInfo.tokenExpiry > Date.now()) return [];

    const result = await axios.get('/api/notifications', {
        headers: { 'X-Auth-Token': authInfo.token },
        validateStatus: false
    });

    if(result.status !== 200) {
        console.log("error while fetching notifs");
        console.log(result.data);
        return [];
    }

    return result.data;
}