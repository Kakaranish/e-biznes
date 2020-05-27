import { connect } from 'react-redux';
import * as AuthActions from '../pages/Auth/duck/actions';
import * as CartActions from '../pages/Cart/ducks/actions';
import * as NotifActions from '../pages/Notifications/ducks/actions';


class AwareComponentBuilder {
    constructor() {
        this.partialMapStateToPropsList = [];
        this.partialDispatchToPropsList = [];
    }

    withAuthAwareness() {
        const partialMapDispatch = dispatch => ({
            logIn: item => dispatch(AuthActions.logIn(item)),
            logOut: () => dispatch(AuthActions.logOut())
        })
        this.partialDispatchToPropsList.push(partialMapDispatch);

        const partialMapState = state => ({
            auth: state.auth
        });
        this.partialMapStateToPropsList.push(partialMapState);
        
        return this;
    }

    withCartAwareness() {
        const partialMapDispatch = dispatch => ({
            setCartItems: cartItems => dispatch(CartActions.setCartItems(cartItems)),
            addToCart: productId => dispatch(CartActions.addToCart(productId)),
            removeFromCart: productId => dispatch(CartActions.removeFromCart(productId)),
            clearCart: () => dispatch(CartActions.clearCart())
        });
        this.partialDispatchToPropsList.push(partialMapDispatch);

        const partialMapState = state => ({
            cartItems: state.cartItems
        });
        this.partialMapStateToPropsList.push(partialMapState);

        return this;
    }

    withNotifsAwareness() {
        const partialMapDispatch = dispatch => ({
            setNotifs: notifs => dispatch(NotifActions.setNotifs(notifs)),
            clearNotifs: () => dispatch(NotifActions.clearNotifs())
        });
        this.partialDispatchToPropsList.push(partialMapDispatch);

        const partialMapState = state => ({
            notifs: state.notifs
        });
        this.partialMapStateToPropsList.push(partialMapState);

        return this;
    }

    build(funcComponent) {

        const mapStateToProps = state => {
            let result = {};
            this.partialMapStateToPropsList.forEach(x => result = Object.assign(result, x(state)));
            return result;
        }

        const mapDispatchToProps = dispatch => {
            let result = {};
            this.partialDispatchToPropsList.forEach(x => result = Object.assign(result, x(dispatch)));
            return result;
        }

        return connect(mapStateToProps, mapDispatchToProps)(funcComponent);
    }
}

export default AwareComponentBuilder;