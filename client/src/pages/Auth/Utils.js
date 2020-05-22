import { connect } from 'react-redux';
import { logIn, logOut } from '../Auth/duck/actions';

export const createAuthAwareComponent = funcComponent => {
    const mapStateToProps = state => ({
        auth: state.auth
    });

    const mapDispatchToProps = dispatch => ({
        logIn: item => dispatch(logIn(item)),
        logOut: () => dispatch(logOut())
    });

    return connect(mapStateToProps, mapDispatchToProps)(funcComponent);
}