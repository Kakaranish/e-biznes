import React, { useState } from 'react';
import { useHistory } from 'react-router-dom';
import axios from 'axios';
import { getFormDataJsonFromEvent, doRequest } from '../../common/Utils';
import AwareComponentBuilder from '../../common/AwareComponentBuilder';

const RegisterPage = (props) => {

    const history = useHistory();

    const onSubmit = async event => {
        event.preventDefault();
        let formData = getFormDataJsonFromEvent(event);
        if (formData.password !== formData.repeatPassword) {
            setValidationErrors(["passwords are different"])
            return;
        }
        formData.repeatPassword = undefined;

        try {
            const action = async () => axios.post('/auth/register', formData,
                { validateStatus: false });
            const result = await doRequest(action);

            const auth = {
                token: result.token,
                tokenExpiry: parseInt(result.tokenExpiry),
                email: result.email,
                role: result.role
            };
            props.logIn(auth);

            setValidationErrors(null);
            history.push('/');
        } catch (error) {
            alert(`${error} error occured`);
        }
    }

    const [validationErrors, setValidationErrors] = useState(null);

    return <>
        <h3>Sign In</h3>
        <form onSubmit={onSubmit}>
            <div className="form-group">
                <input name="email" type="email" className="form-control" id="emailInput" placeholder="Email..." required />
            </div>
            <div className="form-group">
                <input name="firstName" type="text" className="form-control"
                    id="firstNameInput" placeholder="First name..." required />
            </div>
            <div className="form-group">
                <input name="lastName" type="text" className="form-control"
                    id="lastNameInput" placeholder="Last name..." required />
            </div>
            <div className="form-group">
                <input name="password" type="password" className="form-control"
                    id="passwordInput" placeholder="Password..." required />
            </div>

            <div className="form-group">
                <input name="repeatPassword" type="password" className="form-control"
                    id="repepatPasswordInput" placeholder="Repeat password..." required />
            </div>

            <button type="submit" className="btn btn-primary">
                Submit
            </button>

            {
                validationErrors &&
                <div className="col-12 mt-2">
                    <p className="text-danger font-weight-bold" style={{ marginBottom: '0px' }}>
                        Validation errors
                    </p>
                    <ul style={{ paddingTop: "0", marginTop: "0px" }}>
                        {
                            validationErrors.map((error, i) => {
                                return <li key={`val-err-${i}`} className="text-danger">{error}</li>
                            })
                        }
                    </ul>
                </div>
            }
        </form>
    </>
};

export default new AwareComponentBuilder()
    .withAuthAwareness()
    .build(RegisterPage);